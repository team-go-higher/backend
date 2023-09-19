package gohigher.position.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PositionResponse {

	private final Long id;
	private final String position;
}
