package gohigher.oauth2;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import gohigher.usecase.OauthLoginInUseCase;
import gohigher.user.User;
import gohigher.user.oauth2.GoogleOAuth2User;
import gohigher.user.oauth2.KakaoOAuth2User;
import gohigher.user.oauth2.OAuth2UserInfo;
import gohigher.user.oauth2.Provider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthUserService extends DefaultOAuth2UserService {

	private static final String ATTRIBUTE_EMAIL = "email";
	private static final String ROLE_PREFIX = "ROLE_";

	private final OauthLoginInUseCase oauthLoginUseCase;

	@Override
	public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String provider = extractProvider(userRequest);

		OAuth2UserInfo oAuth2UserInfo = convertToMapAttribute(oAuth2User, provider);
		User loginUser = oauthLoginUseCase.login(oAuth2UserInfo.getEmail(), Provider.from(provider));

		return createOAuth2User(oAuth2UserInfo.getAttributes(), loginUser);
	}

	private DefaultOAuth2User createOAuth2User(Map<String, Object> memberAttribute, User loginUser) {
		return new DefaultOAuth2User(
			Collections.singleton(
				new SimpleGrantedAuthority(ROLE_PREFIX.concat(loginUser.getRole().toString()))
			),
			memberAttribute, ATTRIBUTE_EMAIL
		);
	}

	private OAuth2UserInfo convertToMapAttribute(OAuth2User oAuth2User, String provider) {
		if (provider.equals("GOOGLE")) {
			return new GoogleOAuth2User(oAuth2User.getAttributes());
		}
		if (provider.equals("KAKAO")) {
			return new KakaoOAuth2User(oAuth2User.getAttributes());
		}
		throw new IllegalArgumentException("존재하지 않는 provider 입니다.");
	}

	private String extractProvider(OAuth2UserRequest userRequest) {
		return userRequest
			.getClientRegistration()
			.getRegistrationId()
			.toUpperCase();
	}
}
