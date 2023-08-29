package gohigher.user.oauth2;

import java.util.Map;

public class OAuth2UserInfoFactory {

	public static OAuth2UserInfo createFor(Provider provider, Map<String, Object> attributes) {
		return switch (provider) {
			case GOOGLE -> new GoogleOAuth2User(attributes);
			case KAKAO -> new KakaoOAuth2User(attributes);
		};
	}
}
