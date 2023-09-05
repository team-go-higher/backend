package gohigher.oauth2.user;

import java.util.HashMap;
import java.util.Map;

import gohigher.user.auth.Provider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2UserInfoFactory {

	public static OAuth2UserInfo createFor(Provider provider, Map<String, Object> attributes) {
		switch (provider) {
			case GOOGLE:
				Map<String, Object> googleAttributes = new HashMap<>(attributes);
				return new GoogleOAuth2User(googleAttributes);
			case KAKAO:
				return new KakaoOAuth2User(attributes);
			default:
				return null;
		}
	}
}
