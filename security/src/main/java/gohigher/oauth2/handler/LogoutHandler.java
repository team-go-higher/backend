package gohigher.oauth2.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LogoutHandler extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {

	private final String redirectUri;
	private final String logoutRedirectUri;

	public LogoutHandler(@Value("${oauth2.success.redirect_uri}") String redirectUri,
		@Value("${logout.redirect.uri}") String logoutRedirectUri) {
		this.redirectUri = redirectUri;
		this.logoutRedirectUri = logoutRedirectUri;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		String targetUrl = UriComponentsBuilder.fromUriString(redirectUri + logoutRedirectUri)
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUriString();

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
