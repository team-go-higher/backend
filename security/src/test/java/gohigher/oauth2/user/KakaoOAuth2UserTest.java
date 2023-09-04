package gohigher.oauth2.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gohigher.global.exception.GoHigherException;

class KakaoOAuth2UserTest {

	@DisplayName("kakao oauth 사용자 객체 생성")
	@Nested
	class create {

		@DisplayName("kakao oauth 사용자 객체를 생성한다")
		@Test
		void success() {
			HashMap<String, Object> attributes = new HashMap<>();
			attributes.put("kakao_account", new HashMap<>());
			KakaoOAuth2User user = new KakaoOAuth2User(attributes);

			assertAll(
				() -> assertThat(user.oauth2IdAttributeName).isEqualTo("id"),
				() -> assertThat(user.attributes).containsKey("id")
			);
		}

		@DisplayName("kakao oauth 사용자 객체 생성 시 사용자 정보가 없으면 예외가 발생한다")
		@Test
		void userInfoIsEmpty() {
			assertThatThrownBy(() -> new KakaoOAuth2User(new HashMap<>()))
				.isInstanceOf(GoHigherException.class);
		}
	}
}
