package gohigher.support;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	private final SecretKey secretKey;
	private final long accessTokenExpireLength;
	private final long refreshTokenExpireLength;

	public JwtProvider(@Value("${security.jwt.secret-key}") final String secretKey,
		@Value("${security.jwt.expire-length.access}") final long accessTokenExpireLength,
		@Value("${security.jwt.expire-length.refresh}") final long refreshTokenExpireLength) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpireLength = accessTokenExpireLength;
		this.refreshTokenExpireLength = refreshTokenExpireLength;
	}

	public String createAccessToken(final String email, final Date now) {
		return generateToken(email, now, accessTokenExpireLength);
	}

	public String createRefreshToken(final String email, final Date now) {
		return generateToken(email, now, refreshTokenExpireLength);
	}

	private String generateToken(final String email, final Date now, final long expiredLength) {
		Claims claims = Jwts.claims().setSubject(email);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + expiredLength))
			.signWith(secretKey)
			.compact();
	}

	public boolean verifyToken(final String token, final Date now) {
		try {
			Jws<Claims> claims = parseClaimsJws(token);

			return claims.getBody()
				.getExpiration()
				.after(now);
		} catch (Exception e) {
			return false;
		}
	}

	private Jws<Claims> parseClaimsJws(final String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token);
	}
}
