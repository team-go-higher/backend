package gohigher.oauth2.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import gohigher.auth.support.JwtProvider;
import gohigher.auth.support.RefreshTokenCookieProvider;
import gohigher.auth.support.TokenType;
import gohigher.global.exception.GlobalErrorCode;
import gohigher.global.exception.GoHigherException;
import gohigher.user.port.in.TokenCommandPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final String redirectUri;
	private final String tokenRequestUri;
	private final JwtProvider jwtProvider;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;
	private final TokenCommandPort tokenCommandPort;

	public AuthenticationSuccessHandler(@Value("${oauth2.success.redirect_uri}") String redirectUri,
		@Value("${token.request.uri}") String tokenRequestUri, JwtProvider jwtProvider,
		RefreshTokenCookieProvider refreshTokenCookieProvider, TokenCommandPort tokenCommandPort) {
		this.redirectUri = redirectUri;
		this.tokenRequestUri = tokenRequestUri;
		this.jwtProvider = jwtProvider;
		this.refreshTokenCookieProvider = refreshTokenCookieProvider;
		this.tokenCommandPort = tokenCommandPort;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		Long userId = oAuth2User.getAttribute("userId");
		String role = getRole(oAuth2User);

		Date today = new Date();
		String accessToken = jwtProvider.createToken(userId, today, TokenType.ACCESS);
		String refreshToken = jwtProvider.createToken(userId, today, TokenType.REFRESH);

		tokenCommandPort.saveRefreshToken(userId, refreshToken);

		addRefreshTokenCookie(response, refreshToken);
		getRedirectStrategy().sendRedirect(request, response, createTargetUrl(accessToken, role));
	}

	private String getRole(OAuth2User oAuth2User) {
		return oAuth2User.getAuthorities().stream()
			.findFirst()
			.orElseThrow(() -> new GoHigherException(GlobalErrorCode.NOT_CONTROLLED_ERROR))
			.getAuthority();
	}

	private String createTargetUrl(String accessToken, String role) {
		return UriComponentsBuilder.fromUriString(redirectUri + tokenRequestUri)
			.queryParam("accessToken", accessToken)
			.queryParam("role", role)
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUriString();
	}

	private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		ResponseCookie responseCookie = refreshTokenCookieProvider.create(refreshToken);
		response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
	}
}
