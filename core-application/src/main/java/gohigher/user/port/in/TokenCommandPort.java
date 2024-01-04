package gohigher.user.port.in;

public interface TokenCommandPort {

	void saveRefreshToken(Long userId, String refreshToken);
}
