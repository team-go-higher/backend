package gohigher.oauth2.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import gohigher.global.exception.GlobalErrorCode;
import gohigher.global.exception.GoHigherException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final String redirectUri;
	private final String tokenRequestUri;

	public AuthenticationSuccessHandler(@Value("${oauth2.success.redirect_uri}") String redirectUri,
		@Value("${token.request.uri}") String tokenRequestUri) {
		this.redirectUri = redirectUri;
		this.tokenRequestUri = tokenRequestUri;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		Long userId = oAuth2User.getAttribute("userId");
		String role = getRole(oAuth2User);

		String targetUrl = createTargetUrl(userId, role);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private String getRole(OAuth2User oAuth2User) {
		return oAuth2User.getAuthorities().stream()
			.findFirst()
			.orElseThrow(() -> new GoHigherException(GlobalErrorCode.NOT_CONTROLLED_ERROR))
			.getAuthority();
	}

	private String createTargetUrl(Long userId, String role) {
		return UriComponentsBuilder.fromUriString(redirectUri + tokenRequestUri)
			.queryParam("userId", userId)
			.queryParam("role", role)
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUriString();
	}
}
