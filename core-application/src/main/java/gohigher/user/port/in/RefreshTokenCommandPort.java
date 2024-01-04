package gohigher.user.port.in;

public interface RefreshTokenCommandPort {

	void saveRefreshToken(Long userId, String refreshToken);
}
