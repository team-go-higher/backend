package gohigher.oauth2.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gohigher.global.exception.GoHigherException;

@DisplayName("KakaoOAuth2User 클래스의")
class KakaoOAuth2UserTest {

	@DisplayName("생성자는")
	@Nested
	class constructor {

		@DisplayName("사용자 정보가 포함된 attributes 를 입력받을 때")
		@Nested
		class attributes {

			@DisplayName("kakao oauth 사용자 객체를 생성해야 한다.")
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
		}

		@DisplayName("사용자 정보가 포함되어 있지 않은 attributes 를 입력받을 때")
		@Nested
		class empty {

			@DisplayName("예외가 발생해야 한다.")
			@Test
			void fail() {
				assertThatThrownBy(() -> new KakaoOAuth2User(new HashMap<>()))
					.isInstanceOf(GoHigherException.class);
			}
		}
	}
}
