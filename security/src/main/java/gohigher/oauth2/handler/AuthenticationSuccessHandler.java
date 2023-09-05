package gohigher.oauth2.handler;

import static org.springframework.http.HttpHeaders.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import gohigher.global.exception.GlobalErrorCode;
import gohigher.global.exception.GoHigherException;
import gohigher.jwt.support.CookieProvider;
import gohigher.jwt.support.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;
	private final CookieProvider cookieProvider;
	private final String redirectUri;
	private final String tokenRequestUri;

	public AuthenticationSuccessHandler(JwtProvider jwtProvider, CookieProvider cookieProvider,
		@Value("${oauth2.success.redirect_uri}") String redirectUri,
		@Value("${token.request.uri}") String tokenRequestUri) {
		this.jwtProvider = jwtProvider;
		this.cookieProvider = cookieProvider;
		this.redirectUri = redirectUri;
		this.tokenRequestUri = tokenRequestUri;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		Long userId = oAuth2User.getAttribute("userId");
		String role = getRole(oAuth2User);

		Date now = new Date();

		String refreshToken = jwtProvider.createRefreshToken(userId, now);
		addRefreshTokenCookie(response, refreshToken);

		String accessToken = jwtProvider.createAccessToken(userId, now);
		String targetUrl = createTargetUrl(role, accessToken);

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private String getRole(OAuth2User oAuth2User) {
		return oAuth2User.getAuthorities().stream()
			.findFirst()
			.orElseThrow(() -> new GoHigherException(GlobalErrorCode.NOT_CONTROLLED_ERROR))
			.getAuthority();
	}

	private String createTargetUrl(String role, String accessToken) {
		return UriComponentsBuilder.fromUriString(redirectUri + tokenRequestUri)
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
