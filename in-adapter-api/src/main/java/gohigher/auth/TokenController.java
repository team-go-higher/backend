package gohigher.auth;

import static org.springframework.http.HttpHeaders.*;

import java.util.Date;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gohigher.auth.support.RefreshTokenCookieProvider;
import gohigher.controller.response.GohigherResponse;
import gohigher.controller.response.data.TokenResponse;
import gohigher.user.port.in.token.TokenCommandPort;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

	private final TokenCommandPort tokenCommandPort;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;

	@GetMapping
	public ResponseEntity<GohigherResponse<TokenResponse>> login(HttpServletResponse response,
		@RequestParam("userId") Long userId, @RequestParam("role") String role) {
		Date today = new Date();
		String accessToken = tokenCommandPort.createAccessToken(userId, today);
		String refreshToken = tokenCommandPort.createRefreshToken(userId, today);

		addRefreshTokenCookie(response, refreshToken);
		return ResponseEntity.ok(
			GohigherResponse.success(new TokenResponse(accessToken, role)));
	}

	private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		ResponseCookie responseCookie = refreshTokenCookieProvider.create(refreshToken);
		response.addHeader(SET_COOKIE, responseCookie.toString());
	}
}
