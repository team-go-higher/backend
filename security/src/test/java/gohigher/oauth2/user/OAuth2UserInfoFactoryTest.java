package gohigher.oauth2.user;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gohigher.user.Provider;

class OAuth2UserInfoFactoryTest {

	@DisplayName("provider 를 이용하여 적절한 oauth 사용자 객체 생성")
	@Nested
	class createFor {

		@DisplayName("google oauth 사용자 객체를 생성한다")
		@Test
		void google() {
			Provider provider = Provider.GOOGLE;

			OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.createFor(provider, new HashMap<>());

			assertThat(oAuth2UserInfo).isInstanceOf(GoogleOAuth2User.class);
		}

		@DisplayName("kakao oauth 사용자 객체를 생성한다")
		@Test
		void kakao() {
			Provider provider = Provider.KAKAO;

			HashMap<String, Object> attributes = new HashMap<>();
			attributes.put("kakao_account", new HashMap<>());
			OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.createFor(provider, attributes);

			assertThat(oAuth2UserInfo).isInstanceOf(KakaoOAuth2User.class);
		}
	}
}
