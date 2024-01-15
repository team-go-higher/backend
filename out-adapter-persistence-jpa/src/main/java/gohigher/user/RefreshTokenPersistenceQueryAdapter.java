package gohigher.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

import gohigher.user.entity.RefreshTokenJpaEntity;
import gohigher.user.entity.RefreshTokenRepository;
import gohigher.user.port.out.RefreshTokenPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceQueryAdapter implements RefreshTokenPersistenceQueryPort {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public Optional<String> findByUserId(Long userId) {
		return refreshTokenRepository.findByUserId(userId)
			.map(RefreshTokenJpaEntity::getValue);
	}
}
