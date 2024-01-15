package gohigher.support.auth;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import gohigher.global.exception.GoHigherException;
import gohigher.user.auth.AuthErrorCode;
import jakarta.servlet.http.Cookie;

@Component
public class RefreshTokenCookieProvider {

	static final String REFRESH_TOKEN_KEY = "refresh-token";

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

	public String extractToken(Cookie[] cookies) {
		validateCookiesNotEmpty(cookies);
		return Arrays.stream(cookies)
			.filter(it -> it.getName().equals(REFRESH_TOKEN_KEY))
			.findAny()
			.orElseThrow(() -> new GoHigherException(AuthErrorCode.EMPTY_REFRESH_TOKEN_COOKIE))
			.getValue();
	}

	private void validateCookiesNotEmpty(Cookie[] cookies) {
		if (cookies == null) {
			throw new GoHigherException(AuthErrorCode.EMPTY_REFRESH_TOKEN_COOKIE);
		}
	}
}
