package gohigher.user.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.global.exception.GoHigherException;
import gohigher.support.auth.JwtProvider;
import gohigher.support.auth.TokenType;
import gohigher.user.auth.AuthErrorCode;
import gohigher.user.port.in.RefreshedTokenResponse;
import gohigher.user.port.in.TokenCommandPort;
import gohigher.user.port.out.RefreshTokenPersistenceCommandPort;
import gohigher.user.port.out.RefreshTokenPersistenceQueryPort;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenCommandService implements TokenCommandPort {

	private final RefreshTokenPersistenceCommandPort refreshTokenPersistenceCommandport;
	private final RefreshTokenPersistenceQueryPort refreshTokenPersistenceQueryPort;
	private final JwtProvider jwtProvider;

	@Override
	public void saveRefreshToken(Long userId, String refreshToken) {
		refreshTokenPersistenceQueryPort.findByUserId(userId)
			.ifPresent(s -> refreshTokenPersistenceCommandport.delete(userId));

		refreshTokenPersistenceCommandport.save(userId, refreshToken);
	}

	@Override
	public RefreshedTokenResponse refreshToken(Date now, String refreshToken) {
		if (!jwtProvider.verifyToken(refreshToken, now)) {
			throw new GoHigherException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
		}

		Long userId = parseToken(refreshToken);

		String existingRefreshToken = refreshTokenPersistenceQueryPort.findByUserId(userId)
			.orElseThrow(() -> new GoHigherException(AuthErrorCode.NOT_EXISTED_TOKEN));
		validateAlreadyUsedToken(refreshToken, existingRefreshToken);

		String newRefreshToken = jwtProvider.createToken(userId, now, TokenType.REFRESH);
		refreshTokenPersistenceCommandport.update(userId, newRefreshToken);

		return new RefreshedTokenResponse(jwtProvider.createToken(userId, now, TokenType.ACCESS), newRefreshToken);
	}

	@Override
	public void deleteRefreshToken(Long loginId) {
		refreshTokenPersistenceCommandport.delete(loginId);
	}

	private Long parseToken(String refreshToken) {
		try {
			return jwtProvider.getPayload(refreshToken);
		} catch (ExpiredJwtException e) {
			throw new GoHigherException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
		}
	}

	private void validateAlreadyUsedToken(String refreshToken, String existingRefreshToken) {
		if (!refreshToken.equals(existingRefreshToken)) {
			throw new GoHigherException(AuthErrorCode.USED_REFRESH_TOKEN);
		}
	}
}
