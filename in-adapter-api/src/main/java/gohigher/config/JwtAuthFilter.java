package gohigher.config;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import gohigher.port.in.OAuth2QueryPort;
import gohigher.support.AuthorizationExtractor;
import gohigher.support.JwtProvider;
import gohigher.user.User;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final OAuth2QueryPort oAuth2QueryPort;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String accessToken = AuthorizationExtractor.extract(request);
		final Date now = new Date();

		if (!StringUtils.hasText(accessToken)) {
			doFilter(request, response, filterChain);
			return;
		}

		if (!jwtProvider.verifyToken(accessToken, now)) {
			throw new JwtException("Access Token Expired");
		}

		final User user = oAuth2QueryPort.findByEmail(jwtProvider.getUid(accessToken));

		Authentication auth = getAuthentication(user);
		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

	public Authentication getAuthentication(final User user) {
		return new UsernamePasswordAuthenticationToken(user, "",
			List.of(new SimpleGrantedAuthority("ROLE_".concat(user.getRole().toString()))));
	}
}

