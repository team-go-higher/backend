package gohigher.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import gohigher.global.exception.GoHigherException;
import gohigher.support.auth.AuthorizationExtractor;
import gohigher.support.auth.JwtProvider;
import gohigher.user.User;
import gohigher.user.auth.AuthErrorCode;
import gohigher.user.port.in.UserQueryPort;
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

		verifyToken(accessToken, now);
		User user = userQueryPort.findById(jwtProvider.getPayload(accessToken));

		Authentication auth = getAuthentication(user);
		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

	private void verifyToken(String accessToken, Date now) {
		if (!jwtProvider.verifyToken(accessToken, now)) {
			throw new GoHigherException(AuthErrorCode.TOKEN_EXPIRED);
		}
	}

	public Authentication getAuthentication(User user) {
		return new UsernamePasswordAuthenticationToken(user, "",
			List.of(new RoleGrantedAuthority(user.getRole())));
	}
}
