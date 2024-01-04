package gohigher.user;

import org.springframework.stereotype.Component;

import gohigher.user.entity.RefreshTokenJpaEntity;
import gohigher.user.entity.RefreshTokenRepository;
import gohigher.user.port.out.RefreshTokenPersistenceCommandPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceCommandAdapter implements RefreshTokenPersistenceCommandPort {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void save(Long userId, String refreshToken) {
		refreshTokenRepository.save(new RefreshTokenJpaEntity(userId, refreshToken));
	}
}
