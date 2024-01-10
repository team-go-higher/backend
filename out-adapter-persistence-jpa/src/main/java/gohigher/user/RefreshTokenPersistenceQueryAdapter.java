package gohigher.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

import gohigher.global.exception.GoHigherException;
import gohigher.user.auth.AuthErrorCode;
import gohigher.user.entity.RefreshTokenJpaEntity;
import gohigher.user.entity.RefreshTokenRepository;
import gohigher.user.port.out.RefreshTokenPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceQueryAdapter implements RefreshTokenPersistenceQueryPort {

	private RefreshTokenRepository refreshTokenRepository;

	@Override
	public Optional<String> findByUserId(Long userId) {
		RefreshTokenJpaEntity refreshToken = refreshTokenRepository.findByUserId(userId)
			.orElseThrow(() -> new GoHigherException(AuthErrorCode.NOT_EXISTED_TOKEN));
		return Optional.of(refreshToken.getValue());
	}
}
