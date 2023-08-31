package gohigher.support;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import gohigher.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	private final SecretKey secretKey;
	private final long accessTokenExpireLength;
	private final long refreshTokenExpireLength;

	public JwtProvider(@Value("${security.jwt.secret-key}") final String secretKey,
		@Value("${security.jwt.expire-length.access}") final long accessTokenExpireLength,
		@Value("$security.jwt.expire-length.refresh") long refreshTokenExpireLength) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpireLength = accessTokenExpireLength;
		this.refreshTokenExpireLength = refreshTokenExpireLength;
	}

	public String createAccessToken(final String email, final Role role) {
		return generateToken(email, role.toString(), accessTokenExpireLength);
	}

	public String createRefreshToken(final String email, Role role) {
		return generateToken(email, role.toString(), refreshTokenExpireLength);
	}

	public String generateToken(final String email, final String role, final long expiredLength) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("role", role);

		Date now = new Date();

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + expiredLength))
			.signWith(secretKey)
			.compact();

	}
}
