package gohigher.user.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TokenResponse {

	private final String accessToken;
	private final String role;
}
