package gohigher.support.auth;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import gohigher.global.exception.GoHigherException;
import gohigher.user.auth.AuthErrorCode;
import jakarta.servlet.http.Cookie;

@DisplayName("RefreshTokenCookieProvider 클래스의")
class RefreshTokenCookieProviderTest {

	private RefreshTokenCookieProvider refreshTokenCookieProvider = new RefreshTokenCookieProvider(300000,
		"/token", "refresh-token");

	@DisplayName("create 메서드는")
	@Nested
	class Describe_create {

		@DisplayName("만료 기간이 정해지면")
		@Nested
		class Context_adjust_expireLength {

			long expireLength = 300000;

			@DisplayName("해당 기간에 맞는 만료 기간을 가진 쿠키를 반환한다.")
			@Test
			void it_returns_expire_length_cookie() {
				// given
				String value = "value";

				// when
				ResponseCookie cookie = refreshTokenCookieProvider.create(value);

				// then
				assertThat(cookie.getMaxAge()).isEqualTo(Duration.ofMillis(expireLength));
			}
		}

		@DisplayName("value가 정해지면")
		@Nested
		class Context_get_value {

			@DisplayName("해당 value를 가진 쿠키를 반환한다.")
			@Test
			void it_returns_value_cookie() {
				// given
				String value = "value";

				// when
				ResponseCookie cookie = refreshTokenCookieProvider.create(value);

				// then
				assertThat(cookie.getValue()).isEqualTo(value);
			}
		}
	}

	@DisplayName("extractToken 메서드는")
	@Nested
	class Describe_extractToken {

		@DisplayName("쿠키가 주어질 떄,")
		@Nested
		class Context_given_cookies {

			@DisplayName("쿠키에서 리프레시 토큰값을 반환한다.")
			@Test
			void it_returns_refresh_token_value() {
				// given
				String refreshTokenValue = "refreshTokenValue";
				Cookie[] cookies = new Cookie[] {
					new Cookie(refreshTokenCookieProvider.getRefreshTokenKey(), refreshTokenValue)};

				// when
				String token = refreshTokenCookieProvider.extractToken(cookies);

				// then
				assertThat(token).isEqualTo(refreshTokenValue);
			}

			@DisplayName("리프레시 토큰 쿠키가 없다면 예외를 발생한다.")
			@Test
			void it_returns_exception_if_no_refresh_token_cookie() {
				// given
				Cookie[] cookies = new Cookie[] {new Cookie("fake", "value")};

				// when && then
				assertThatThrownBy(() -> refreshTokenCookieProvider.extractToken(cookies)).isInstanceOf(
					GoHigherException.class).hasMessage(AuthErrorCode.EMPTY_REFRESH_TOKEN_COOKIE.getMessage());
			}
		}
	}
}
