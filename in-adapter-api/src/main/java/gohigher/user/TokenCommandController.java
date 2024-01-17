package gohigher.user;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import gohigher.controller.response.GohigherResponse;
import gohigher.support.auth.Login;
import gohigher.support.auth.RefreshTokenCookieProvider;
import gohigher.user.port.in.RefreshedTokenResponse;
import gohigher.user.port.in.TokenCommandPort;
import gohigher.user.port.in.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TokenCommandController implements TokenCommandControllerDocs {

	private final TokenCommandPort tokenCommandPort;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;

	@PatchMapping("/tokens/mine")
	public ResponseEntity<GohigherResponse<TokenResponse>> reissueRefreshTokens(HttpServletRequest request,
		HttpServletResponse response, @Login Long userId) {
		String refreshToken = refreshTokenCookieProvider.extractToken(request.getCookies());
		Date now = new Date();
		RefreshedTokenResponse refreshedTokenResponse = tokenCommandPort.refreshToken(userId, now, refreshToken);
		TokenResponse tokenResponse = new TokenResponse(refreshedTokenResponse.getAccessToken());
		addRefreshTokenCookie(response, refreshedTokenResponse.getRefreshToken());
		return ResponseEntity.ok(GohigherResponse.success(tokenResponse));
	}

	private void addRefreshTokenCookie(HttpServletResponse response, String refreshedToken) {
		ResponseCookie responseCookie = refreshTokenCookieProvider.create(refreshedToken);
		response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
	}
}
