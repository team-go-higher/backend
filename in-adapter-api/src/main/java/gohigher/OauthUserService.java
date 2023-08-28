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

	@Override
	public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
		String providerId = extractProviderId(userRequest);

		Map<String, Object> memberAttribute = convertToMapAttribute(oAuth2User, providerId, userRequest);
		String email = (String)memberAttribute.get(ATTRIBUTE_EMAIL);

		User loginUser = oauthLoginUseCase.login(email, Provider.from(providerId));
		return createOAuth2User(memberAttribute, loginUser);

	}

	private DefaultOAuth2User createOAuth2User(Map<String, Object> memberAttribute, User loginUser) {
		return new DefaultOAuth2User(
			Collections.singleton(
				new SimpleGrantedAuthority(ROLE_PREFIX.concat(loginUser.getRole().toString()))
			),
			memberAttribute, ATTRIBUTE_EMAIL
		);
	}

	private Map<String, Object> convertToMapAttribute(OAuth2User oAuth2User, String providerId, OAuth2UserRequest userRequest) {
		return OAuth2Attribute.of(
				providerId,
				extractAttributeKey(userRequest),
				oAuth2User.getAttributes()
			)
			.convertToMap();
	}

	private String extractProviderId(OAuth2UserRequest userRequest) {
		return userRequest
			.getClientRegistration()
			.getRegistrationId();

	}

	private String extractAttributeKey(OAuth2UserRequest userRequest) {
		return userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
	}
}
