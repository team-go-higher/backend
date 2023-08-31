package gohigher.support;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

class CookieProviderTest {

	private static final int REFRESH_TOKEN_EXPIRE_LENGTH = 300000;

	@DisplayName("리프레시 토큰 Cookie가 잘 만들어지는지 확인한다.")
	@Test
	void create() {
		// given
		CookieProvider cookieProvider = new CookieProvider(REFRESH_TOKEN_EXPIRE_LENGTH);
		String refreshToken = "refreshToken";

		// when
		ResponseCookie refreshTokenCookie = cookieProvider.create(refreshToken);

		// then
		assertAll(
			() -> assertThat(refreshTokenCookie.getMaxAge()).isEqualTo(Duration.ofMillis(REFRESH_TOKEN_EXPIRE_LENGTH)),
			() -> assertThat(refreshTokenCookie.getValue()).isEqualTo(refreshToken)
		);
	}
}
