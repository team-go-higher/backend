package gohigher.oauth2;

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

import gohigher.support.CookieProvider;
import gohigher.support.JwtProvider;
import gohigher.user.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;
	private final CookieProvider cookieProvider;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
		final Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		String email = oAuth2User.getAttribute("email");
		String role = oAuth2User.getAuthorities().stream()
			.findFirst()
			.orElseThrow(IllegalAccessError::new) // 존재하지 않을 시 예외를 던진다.
			.getAuthority();

		String requestUri = "http://localhost:3000/login";
		Date now = new Date();

		String accessToken = jwtProvider.createAccessToken(email, now);
		String targetUrl = createTargetUrl(role, requestUri, accessToken);
		addRefreshTokenCookie(response, email, role, now);

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private String createTargetUrl(final String role, final String requestUri, final String accessToken) {
		return UriComponentsBuilder.fromUriString(requestUri)
			.queryParam("accessToken", accessToken)
			.queryParam("role", Role.valueOf(role).toString())
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUriString();
	}

	private void addRefreshTokenCookie(final HttpServletResponse response, final String email, final String role,
		final Date now) {
		if (role.equals(Role.GUEST.toString())) {
			return;
		}

		String refreshToken = jwtProvider.createRefreshToken(email, now);
		ResponseCookie responseCookie = cookieProvider.create(refreshToken);
		response.addHeader(SET_COOKIE, responseCookie.toString());
	}
}
