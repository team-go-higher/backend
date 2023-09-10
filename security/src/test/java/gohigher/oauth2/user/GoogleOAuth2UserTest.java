package gohigher.oauth2.user;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("GoogleOAuth2User 클래스의")
class GoogleOAuth2UserTest {

	@DisplayName("생성자는")
	@Nested
	class constructor {

		@DisplayName("attributes 를 입력받을 때")
		@Nested
		class attributes {

			@DisplayName("google oauth 사용자 객체를 생성해야 한다.")
			@Test
			void success() {
				// given & when
				GoogleOAuth2User user = new GoogleOAuth2User(new HashMap<>());

				// then
				assertThat(user.oauth2IdAttributeName).isEqualTo("sub");
			}
		}
	}
}
