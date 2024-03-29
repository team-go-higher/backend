package gohigher.user.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RefreshedTokenResponse {

	private final String accessToken;
	private final String refreshToken;
}
