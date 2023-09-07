package gohigher.auth.support;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gohigher.global.exception.GoHigherException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@DisplayName("JwtProvider 클래스의")
class JwtProviderTest {

	private static final long ACCESS_TOKEN_EXPIRE_LENGTH = 100000;
	private static final int REFRESH_TOKEN_EXPIRE_LENGTH = 300000;
	private static final int UNIT_TO_CONVERT_MILLI_TO_SECOND = 1000;
	private static final String SECRET = "secretKey".repeat(6);

	private final JwtProvider jwtProvider = new JwtProvider(SECRET, ACCESS_TOKEN_EXPIRE_LENGTH,
		REFRESH_TOKEN_EXPIRE_LENGTH);

	@DisplayName("createToken 메서드는")
	@Nested
	class createToken {

		@DisplayName("value가 정해지면")
		@Nested
		class Value {
			Long userId = 1L;
			Date now = new Date();

			@DisplayName("엑세스 토큰이 정상적으로 만든다")
			@Test
			void access_token_success() {
				// given & when
				String accessToken = jwtProvider.createToken(userId, now, TokenType.ACCESS);

				// then
				Claims claims = Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
					.build()
					.parseClaimsJws(accessToken)
					.getBody();

				assertAll(
					() -> assertThat(claims.getSubject()).isEqualTo(String.valueOf(userId)),
					() -> {
						long makingTokenTime = now.getTime() / 1000 * 1000;
						assertThat(claims.getExpiration()).isEqualTo(
							new Date(makingTokenTime + ACCESS_TOKEN_EXPIRE_LENGTH));
					}
				);
			}

			@DisplayName("리프레시 토큰이 정상적으로 만든다")
			@Test
			void refresh_token_success() {
				// given & when
				String refreshToken = jwtProvider.createToken(userId, now, TokenType.REFRESH);

				// then
				Claims claims = Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
					.build()
					.parseClaimsJws(refreshToken)
					.getBody();

				assertAll(
					() -> assertThat(claims.getSubject()).isEqualTo(String.valueOf(userId)),
					() -> {
						long makingTokenTime = now.getTime() / 1000 * 1000;
						assertThat(claims.getExpiration()).isEqualTo(
							new Date(makingTokenTime + REFRESH_TOKEN_EXPIRE_LENGTH));
					}
				);
			}
		}

	}

	@DisplayName("verifyToken 메서드는")
	@Nested
	class verifyToken {

		private final Long userId = 1L;
		private final Date now = new Date();

		@DisplayName("만료 기간이 주어질 때")
		@Nested
		class expired_length {

			@DisplayName("만료 기간이 지나지 않았다면 true를 리턴한다")
			@Test
			void returns_true_valid_time() {
				// given & when
				String accessToken = jwtProvider.createToken(userId, now, TokenType.ACCESS);

				// then
				assertThat(jwtProvider.verifyToken(accessToken,
					new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH - UNIT_TO_CONVERT_MILLI_TO_SECOND))).isTrue();
			}

			@DisplayName("만료 기간이 지났다면 false를 리턴한다")
			@Test
			void returns_false_invalid_time() {
				// given & when
				String accessToken = jwtProvider.createToken(userId, now, TokenType.ACCESS);

				// then
				assertThat(jwtProvider.verifyToken(accessToken,
					new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH))).isFalse();
			}
		}

		@DisplayName("토큰이 주어질 떄")
		@Nested
		class secret_key {

			@DisplayName("다른 secret key로 생성된 토큰이라면 예외를 발생한다")
			@Test
			void returns_false_different_secret_key() {
				// given
				JwtProvider differentJwtProvider = new JwtProvider("differentKey".repeat(5), ACCESS_TOKEN_EXPIRE_LENGTH,
					REFRESH_TOKEN_EXPIRE_LENGTH);
				String token = differentJwtProvider.createToken(userId, now, TokenType.ACCESS);

				// when & then
				assertThatThrownBy(() -> jwtProvider.verifyToken(token, now)).isInstanceOf(GoHigherException.class);
			}
		}
	}
}

