package gohigher.auth.support;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieProvider {

	private static final String REFRESH_TOKEN_KEY = "refresh-token";

	private final long refreshTokenExpireLength;
	private final String tokenRequestUri;

	public RefreshTokenCookieProvider(@Value("${security.jwt.expire-length.refresh}") long refreshTokenExpireLength,
		@Value("${token.request.uri}") String tokenRequestUri) {
		this.refreshTokenExpireLength = refreshTokenExpireLength;
		this.tokenRequestUri = tokenRequestUri;
	}

	public ResponseCookie create(String refreshToken) {
		return createTokenCookieBuilder(refreshToken)
			.maxAge(Duration.ofMillis(refreshTokenExpireLength))
			.build();
	}

	private ResponseCookie.ResponseCookieBuilder createTokenCookieBuilder(String value) {
		return ResponseCookie.from(REFRESH_TOKEN_KEY, value)
			.httpOnly(true)
			.secure(true)
			.path(tokenRequestUri)
			.sameSite(SameSite.NONE.attributeValue());
	}
}
