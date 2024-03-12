package gohigher.user.port.in;

import java.util.Date;

public interface TokenCommandPort {

	void saveRefreshToken(Long userId, String refreshToken);

	RefreshedTokenResponse refreshToken(Date now, String refreshToken);

	void deleteRefreshToken(Long loginId);
}
