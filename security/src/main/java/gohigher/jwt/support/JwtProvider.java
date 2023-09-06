package gohigher.jwt.support;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import gohigher.global.exception.GoHigherException;
import gohigher.user.auth.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

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

	public String createAccessToken(Long userId, Date now) {
		return generateToken(userId, now, accessTokenExpireLength);
	}

	public String createRefreshToken(Long userId, Date now) {
		return generateToken(userId, now, refreshTokenExpireLength);
	}

	private String generateToken(Long userId, Date now, long expiredLength) {
		Claims claims = Jwts.claims().setSubject(String.valueOf(userId));

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
		} catch (SignatureException e) {
			throw new GoHigherException(AuthErrorCode.INVALID_TOKEN_BY_SIGNATURE);
		}
	}

	private Jws<Claims> parseClaimsJws(String token) throws SignatureException {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token);
	}

	public Long getPayload(String token) {
		String payload = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
		return Long.valueOf(payload);
	}
}