package gohigher.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.global.exception.GoHigherException;
import gohigher.support.auth.JwtProvider;
import gohigher.support.auth.TokenType;
import gohigher.user.auth.AuthErrorCode;
import gohigher.user.port.out.RefreshTokenPersistenceCommandPort;
import gohigher.user.port.out.RefreshTokenPersistenceQueryPort;

@DisplayName("TokenCommandService 클래스의")
@ExtendWith(MockitoExtension.class)
class TokenCommandServiceTest {

	private static final int ACCESS_TOKEN_EXPIRE_LENGTH = 60000;
	private static final int REFRESH_TOKEN_EXPIRE_LENGTH = 60000;
	private final JwtProvider jwtProvider = new JwtProvider(
		"c482e22dd637030da080d5ebf9864988a04b9e0940aabd77a0ad4b67f09463a1192b946701cb0a836ab2c962dd6bcf2094448d0f817592f2bde706e41dc5257b",
		ACCESS_TOKEN_EXPIRE_LENGTH, REFRESH_TOKEN_EXPIRE_LENGTH);
	@Mock
	private RefreshTokenPersistenceCommandPort refreshTokenPersistenceCommandPort;
	@Mock
	private RefreshTokenPersistenceQueryPort refreshTokenPersistenceQueryPort;
	private TokenCommandService tokenCommandService;

	@BeforeEach
	void setUp() {
		tokenCommandService = new TokenCommandService(refreshTokenPersistenceCommandPort,
			refreshTokenPersistenceQueryPort, jwtProvider);
	}

	@DisplayName("refreshToken 메서드는")
	@Nested
	class Describe_refreshToken {

		@DisplayName("리프레시 토큰과 user의 id를 받으면,")
		@Nested
		class Context_request_with_token {

			@DisplayName("유효성 확인 후 정상이라면 토큰을 재발급한다.")
			@Test
			void it_returns_new_access_token_and_refresh_token() {
				// given
				Long userId = 1L;
				Date now = new Date();
				String previousRefreshToken = jwtProvider.createToken(userId, now, TokenType.REFRESH);

				// when
				given(refreshTokenPersistenceQueryPort.findByUserId(userId)).willReturn(
					Optional.of(previousRefreshToken));
				doNothing().when(refreshTokenPersistenceCommandPort).update(anyLong(), anyString());

				// then
				assertThat(tokenCommandService.refreshToken(userId, now, previousRefreshToken)).isNotNull();
			}

			@DisplayName("유효성 확인 후, 이미 사용한 토큰이면 예외를 발생한다.")
			@Test
			void it_throws_Exception_if_token_is_already_used() {
				Long userId = 1L;
				Date now = new Date();
				String previousRefreshToken = "previousRefreshToken";

				// when
				given(refreshTokenPersistenceQueryPort.findByUserId(userId)).willReturn(
					Optional.of(previousRefreshToken));

				// then
				String anotherToken = jwtProvider.createToken(userId, now, TokenType.REFRESH);
				assertThatThrownBy(() -> tokenCommandService.refreshToken(userId, now, anotherToken))
					.isInstanceOf(GoHigherException.class)
					.hasMessage(AuthErrorCode.USED_REFRESH_TOKEN.getMessage());
			}
		}
	}
}
