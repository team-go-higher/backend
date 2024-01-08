package gohigher.user.port.out;

public interface RefreshTokenPersistenceCommandPort {

	void save(Long userId, String refreshToken);

	void update(Long userId, String refreshToken);
}
