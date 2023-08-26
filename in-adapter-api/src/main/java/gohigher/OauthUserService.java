package gohigher;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import gohigher.usecase.OauthLoginInUseCase;
import gohigher.user.Provider;
import gohigher.user.User;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private static final String ATTRIBUTE_EMAIL = "email";
	private static final String ROLE_PREFIX = "ROLE_";

	private final OauthLoginInUseCase oauthLoginUseCase;

	@SuppressWarnings("checkstyle:WhitespaceAround")
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

		// 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성을 가져온다.
		String providerId = extractProviderId(userRequest);
		String attributeKey = extractAttributeKey(userRequest);

		// OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
		Map<String, Object> memberAttribute = getAttribute(oAuth2User, providerId, attributeKey);

		String email = (String)memberAttribute.get(ATTRIBUTE_EMAIL);

		User loginUser = oauthLoginUseCase.login(email, Provider.from(providerId));
		return new DefaultOAuth2User(
			Collections.singleton(
				new SimpleGrantedAuthority(ROLE_PREFIX.concat(loginUser.getRole().toString()))),
			memberAttribute, ATTRIBUTE_EMAIL);

	}

	private Map<String, Object> getAttribute(OAuth2User oAuth2User, String providerId,
		String userNameAttributeName) {
		OAuth2Attribute oAuth2Attribute =
			OAuth2Attribute.of(providerId, userNameAttributeName, oAuth2User.getAttributes());

		// OAuth2Attribute의 속성값들을 Map으로 반환 받는다.
		return oAuth2Attribute.convertToMap();
	}

	private String extractProviderId(final OAuth2UserRequest userRequest) {
		return userRequest
			.getClientRegistration()
			.getRegistrationId();

	}

	private String extractAttributeKey(final OAuth2UserRequest userRequest) {
		return userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
	}
}
