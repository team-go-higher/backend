package gohigher.oauth2.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import gohigher.support.auth.JwtProvider;
import gohigher.support.auth.RefreshTokenCookieProvider;
import gohigher.user.port.in.TokenCommandPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogoutHandler implements LogoutSuccessHandler {

	private final RefreshTokenCookieProvider refreshTokenCookieProvider;
	private final JwtProvider jwtProvider;
	private final TokenCommandPort tokenCommandPort;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		String refreshToken = refreshTokenCookieProvider.extractToken(request.getCookies());
		Long loginId = jwtProvider.getPayload(refreshToken);
		tokenCommandPort.deleteRefreshToken(loginId);

		ResponseCookie refreshCookie = refreshTokenCookieProvider.createInvalidCookie();
		response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

		response.setStatus(HttpStatus.NO_CONTENT.value());
	}
}
