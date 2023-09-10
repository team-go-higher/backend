package gohigher.oauth2.user;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gohigher.user.auth.Provider;

@DisplayName("OAuth2UserInfoFactory 클래스의")
class OAuth2UserInfoFactoryTest {

	@DisplayName("createFor 메서드는")
	@Nested
	class createFor {

		@DisplayName("google provider 를 입력받을 때")
		@Nested
		class google {

			@DisplayName("google oauth 사용자 객체를 생성한다")
			@Test
			void success() {
				// given
				Provider provider = Provider.GOOGLE;

				// when
				OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.createFor(provider, new HashMap<>());

				// then
				assertThat(oAuth2UserInfo).isInstanceOf(GoogleOAuth2User.class);
			}
		}

		@DisplayName("kakao provider 를 입력받을 때")
		@Nested
		class kakao {

			@DisplayName("kakao oauth 사용자 객체를 생성한다")
			@Test
			void success() {
				// given
				Provider provider = Provider.KAKAO;
				HashMap<String, Object> attributes = new HashMap<>();
				attributes.put("kakao_account", new HashMap<>());

				// when
				OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.createFor(provider, attributes);

				// then
				assertThat(oAuth2UserInfo).isInstanceOf(KakaoOAuth2User.class);
			}
		}
	}
}
