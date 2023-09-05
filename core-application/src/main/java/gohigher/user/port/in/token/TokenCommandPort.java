package gohigher.user.port.in.token;

import java.util.Date;

public interface TokenCommandPort {

	String createAccessToken(Long userId, Date now);

	String createRefreshToken(Long userId, Date now);

	boolean verify(String token, Date now);

	Long getPayload(String token);
}
