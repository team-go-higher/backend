package gohigher.usecase.token;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtCommandServiceTest {

	private static final int ACCESS_TOKEN_EXPIRE_LENGTH = 100000;
	private static final int REFRESH_TOKEN_EXPIRE_LENGTH = 300000;
	private static final int UNIT_TO_CONVERT_MILLI_TO_SECOND = 1000;
	private static final String SECRET =
		"1b5f8fb17f30171fdc794a47e35d284e0ef047c90a09571b2a3914eb5c0cd1e798d5d4a8a0b8f295dc4588a9e3d87907fed168e32348c56a346a8f1ada76b82e";

	private JwtCommandService jwtCommandService;

	@BeforeEach
	void setUp() {
		jwtCommandService = new JwtCommandService(SECRET, ACCESS_TOKEN_EXPIRE_LENGTH, REFRESH_TOKEN_EXPIRE_LENGTH);
	}

	@DisplayName("엑세스 토큰이 정상적으로 만들어지는지 확인한다.")
	@Test
	void createAccessToken() {
		// given
		Long userId = 1L;

		// when
		String accessToken = jwtCommandService.createAccessToken(userId, new Date());

		// then
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
			.build()
			.parseClaimsJws(accessToken)
			.getBody();

		assertThat(claims.getSubject()).isEqualTo(String.valueOf(userId));
	}

	@DisplayName("토큰을 검증한다.")
	@Test
	void verify() {
		// given
		Long userId = 1L;

		// when
		Date now = new Date();
		String accessToken = jwtCommandService.createAccessToken(userId, now);

		// then
		assertAll(
			() -> assertThat(
				jwtCommandService.verify(accessToken, new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH)))
				.isFalse(),
			() -> assertThat(jwtCommandService.verify(accessToken,
				new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH - UNIT_TO_CONVERT_MILLI_TO_SECOND))).isTrue()
		);
	}
}
