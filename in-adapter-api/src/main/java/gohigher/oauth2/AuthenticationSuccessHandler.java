package gohigher.oauth2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import gohigher.support.JwtProvider;
import gohigher.user.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		String role = oAuth2User.getAuthorities().stream()
			.findFirst()
			.orElseThrow(IllegalAccessError::new) // 존재하지 않을 시 예외를 던진다.
			.getAuthority();

		String requestUri = request.getRequestURI();
		Date now = new Date();

		String accessToken = jwtProvider.createAccessToken(oAuth2User.getAttribute("email"), now);
		String targetUrl = UriComponentsBuilder.fromUriString(requestUri)
			.queryParam("accessToken", accessToken)
			.queryParam("role", Role.valueOf(role).toString())
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUriString();

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
