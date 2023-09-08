package gohigher.auth;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gohigher.auth.response.TokenResponse;
import gohigher.auth.support.JwtProvider;
import gohigher.auth.support.RefreshTokenCookieProvider;
import gohigher.auth.support.TokenType;
import gohigher.controller.response.GohigherResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController implements TokenControllerDocs {

	private final JwtProvider jwtProvider;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;

	@GetMapping
	public ResponseEntity<GohigherResponse<TokenResponse>> authorize(HttpServletResponse response,
		@RequestParam("userId") Long userId, @RequestParam("role") String role) {
		Date today = new Date();
		String accessToken = jwtProvider.createToken(userId, today, TokenType.ACCESS);
		addRefreshTokenCookie(response, jwtProvider.createToken(userId, today, TokenType.REFRESH));

		TokenResponse tokenResponse = new TokenResponse(accessToken, role);
		return ResponseEntity.ok(GohigherResponse.success(tokenResponse));
	}

	private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		ResponseCookie responseCookie = refreshTokenCookieProvider.create(refreshToken);
		response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
	}
}
