package gohigher.jwt;

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

import gohigher.jwt.support.AuthorizationExtractor;
import gohigher.jwt.support.JwtProvider;
import gohigher.port.in.UserQueryPort;
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
	private final UserQueryPort userQueryPort;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String accessToken = AuthorizationExtractor.extract(request);
		Date now = new Date();

		if (!StringUtils.hasText(accessToken)) {
			doFilter(request, response, filterChain);
			return;
		}

		if (!jwtProvider.verifyToken(accessToken, now)) {
			throw new JwtException("Access Token Expired");
		}

		User user = userQueryPort.findByEmail(jwtProvider.getUid(accessToken));

		Authentication auth = getAuthentication(user);
		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

	public Authentication getAuthentication(User user) {
		return new UsernamePasswordAuthenticationToken(user, "",
			List.of(new SimpleGrantedAuthority("ROLE_".concat(user.getRole().toString()))));
	}
}

