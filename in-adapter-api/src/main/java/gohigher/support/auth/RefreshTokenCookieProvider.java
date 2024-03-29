package gohigher.support.auth;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;

import gohigher.global.exception.GoHigherException;
import gohigher.user.auth.AuthErrorCode;
import jakarta.servlet.http.Cookie;

@Component
public class RefreshTokenCookieProvider {

	private final long refreshTokenExpireLength;
	private final String tokenRequestUri;
	private final String refreshTokenKey;

	public RefreshTokenCookieProvider(@Value("${security.jwt.expire-length.refresh}") long refreshTokenExpireLength,
		@Value("${token.request.uri}") String tokenRequestUri,
		@Value("${token.cookie.key}") String refreshTokenKey) {
		this.refreshTokenExpireLength = refreshTokenExpireLength;
		this.tokenRequestUri = tokenRequestUri;
		this.refreshTokenKey = refreshTokenKey;
	}

	public ResponseCookie create(String refreshToken) {
		return createTokenCookieBuilder(refreshToken)
			.maxAge(Duration.ofMillis(refreshTokenExpireLength))
			.build();
	}

	private ResponseCookieBuilder createTokenCookieBuilder(String value) {
		return ResponseCookie.from(refreshTokenKey, value)
			.httpOnly(true)
			.secure(true)
			.path(tokenRequestUri)
			.sameSite(SameSite.NONE.attributeValue());
	}

	public String extractToken(Cookie[] cookies) {
		validateCookiesNotEmpty(cookies);
		return Arrays.stream(cookies)
			.filter(it -> it.getName().equals(refreshTokenKey))
			.findAny()
			.orElseThrow(() -> new GoHigherException(AuthErrorCode.EMPTY_REFRESH_TOKEN_COOKIE))
			.getValue();
	}

	public ResponseCookie createInvalidCookie() {
		return createTokenCookieBuilder("")
			.maxAge(Duration.ofMillis(0))
			.build();
	}

	private void validateCookiesNotEmpty(Cookie[] cookies) {
		if (cookies == null) {
			throw new GoHigherException(AuthErrorCode.EMPTY_REFRESH_TOKEN_COOKIE);
		}
	}

	public String getRefreshTokenKey() {
		return refreshTokenKey;
	}
}
