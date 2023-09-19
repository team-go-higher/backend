package gohigher.position.port.in;

import gohigher.position.Position;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PositionResponse {

	private final Long id;
	private final String position;

	public static PositionResponse from(Position position) {
		return new PositionResponse(position.getId(), position.getValue());
	}
}
