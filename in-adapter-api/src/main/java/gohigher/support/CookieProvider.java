package gohigher.support;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

	private static final String REFRESH_TOKEN = "refreshToken";
	private final long refreshTokenExpireLength;

	public CookieProvider(@Value("${security.jwt.expire-length.refresh}") final long refreshTokenExpireLength) {
		this.refreshTokenExpireLength = refreshTokenExpireLength;
	}

	public ResponseCookie create(final String refreshToken) {
		return createTokenCookieBuilder(refreshToken)
			.maxAge(Duration.ofMillis(refreshTokenExpireLength))
			.build();
	}

	private ResponseCookie.ResponseCookieBuilder createTokenCookieBuilder(final String value) {
		return ResponseCookie.from(REFRESH_TOKEN, value)
			.httpOnly(true)
			.secure(true)
			.path("/token")
			.sameSite(SameSite.NONE.attributeValue());
	}
}
