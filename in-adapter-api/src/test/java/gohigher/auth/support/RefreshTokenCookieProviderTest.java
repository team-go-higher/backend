package gohigher.auth.support;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

@DisplayName("RefreshTokenCookieProvider 클래스의")
class RefreshTokenCookieProviderTest {

	@DisplayName("create 메서드는")
	@Nested
	class Create {

		@DisplayName("만료 기간이 정해지면")
		@Nested
		class ExpireLength {

			long expireLength = 300000;

			@DisplayName("해당 기간에 맞는 만료 기간을 가진 쿠키를 반환한다.")
			@Test
			void returns_expire_length_cookie() {
				// given
				RefreshTokenCookieProvider refreshTokenCookieProvider = new RefreshTokenCookieProvider(expireLength,
					"/token");
				String value = "value";

				// when
				ResponseCookie cookie = refreshTokenCookieProvider.create(value);

				// then
				assertThat(cookie.getMaxAge()).isEqualTo(Duration.ofMillis(expireLength));
			}
		}

		@DisplayName("value가 정해지면")
		@Nested
		class Value {

			@DisplayName("해당 value를 가진 쿠키를 반환한다.")
			@Test
			void returns_value_cookie() {
				// given
				RefreshTokenCookieProvider refreshTokenCookieProvider = new RefreshTokenCookieProvider(300000,
					"/token");
				String value = "value";

				// when
				ResponseCookie cookie = refreshTokenCookieProvider.create(value);

				// then
				assertThat(cookie.getValue()).isEqualTo(value);
			}
		}
	}
}
