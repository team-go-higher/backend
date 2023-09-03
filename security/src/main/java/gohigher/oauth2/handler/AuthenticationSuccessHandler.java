package gohigher.oauth2.handler;

import static org.springframework.http.HttpHeaders.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import gohigher.jwt.support.CookieProvider;
import gohigher.jwt.support.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;
	private final CookieProvider cookieProvider;
	private final String redirectUrl = "http://localhost:3000/token";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		Long userId = oAuth2User.getAttribute("userId");
		String role = oAuth2User.getAuthorities().stream()
			.findFirst()
			.orElseThrow(IllegalAccessError::new)
			.getAuthority();

		Date now = new Date();

		String accessToken = jwtProvider.createAccessToken(userId, now);
		String refreshToken = jwtProvider.createRefreshToken(userId, now);
		String targetUrl = createTargetUrl(role, accessToken);

		addRefreshTokenCookie(response, refreshToken);

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private String createTargetUrl(String role, String accessToken) {
		return UriComponentsBuilder.fromUriString(redirectUrl)
			.queryParam("accessToken", accessToken)
			.queryParam("role", role)
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUriString();
	}

	private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		ResponseCookie responseCookie = cookieProvider.create(refreshToken);
		response.addHeader(SET_COOKIE, responseCookie.toString());
	}
}
