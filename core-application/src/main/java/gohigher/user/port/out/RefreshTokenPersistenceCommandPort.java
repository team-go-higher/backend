package gohigher.user.port.out;

public interface RefreshTokenPersistenceCommandPort {

	void save(Long userId, String refreshToken);
}
