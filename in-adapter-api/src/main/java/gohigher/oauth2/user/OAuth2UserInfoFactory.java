package gohigher.oauth2.user;

import java.util.Map;

import gohigher.user.Provider;

public class OAuth2UserInfoFactory {

	public static OAuth2UserInfo createFor(final Provider provider, final Map<String, Object> attributes) {
		return switch (provider) {
			case GOOGLE -> new GoogleOAuth2User(attributes);
			case KAKAO -> new KakaoOAuth2User(attributes);
		};
	}
}
