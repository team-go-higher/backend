package gohigher.jwt.support;

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

	public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey,
		@Value("${security.jwt.expire-length.access}") long accessTokenExpireLength,
		@Value("${security.jwt.expire-length.refresh}") long refreshTokenExpireLength) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpireLength = accessTokenExpireLength;
		this.refreshTokenExpireLength = refreshTokenExpireLength;
	}

	public String createAccessToken(String email, Date now) {
		return generateToken(email, now, accessTokenExpireLength);
	}

	public String createRefreshToken(String email, Date now) {
		return generateToken(email, now, refreshTokenExpireLength);
	}

	private String generateToken(String email, Date now, long expiredLength) {
		Claims claims = Jwts.claims().setSubject(email);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + expiredLength))
			.signWith(secretKey)
			.compact();
	}

	public boolean verifyToken(String token, Date now) {
		try {
			Jws<Claims> claims = parseClaimsJws(token);

			return claims.getBody()
				.getExpiration()
				.after(now);
		} catch (Exception e) {
			return false;
		}
	}

	private Jws<Claims> parseClaimsJws(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token);
	}

	public String getUid(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}
}
