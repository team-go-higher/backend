package gohigher.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.user.port.in.TokenCommandPort;
import gohigher.user.port.out.RefreshTokenPersistenceCommandPort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenCommandService implements TokenCommandPort {

	private final RefreshTokenPersistenceCommandPort refreshTokenPersistenceCommandport;

	@Override
	public void saveRefreshToken(Long userId, String refreshToken) {
		refreshTokenPersistenceCommandport.save(userId, refreshToken);
	}
}
