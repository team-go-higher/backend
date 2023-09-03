package gohigher.oauth2.user;

import java.util.Map;

import gohigher.user.oauth2.Provider;

public class OAuth2UserInfoFactory {

	public static OAuth2UserInfo createFor(Provider provider, Map<String, Object> attributes) {
		return switch (provider) {
			case GOOGLE -> new GoogleOAuth2User(attributes);
			case KAKAO -> new KakaoOAuth2User(attributes);
		};
	}
}
