package gohigher.user.port.in;

import java.util.List;
import java.util.stream.Collectors;

import gohigher.position.port.in.PositionResponse;
import gohigher.user.DesiredPositions;
import gohigher.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyInfoResponse {
	private final String email;
	private final List<PositionResponse> desiredPositions;

	public static MyInfoResponse from(User user) {
		DesiredPositions desiredPositions = user.getDesiredPositions();
		List<PositionResponse> positionResponses = desiredPositions.getSubPositions().stream()
			.map(PositionResponse::from)
			.collect(Collectors.toList());
		positionResponses.add(0, PositionResponse.from(desiredPositions.getMainPosition()));
		return new MyInfoResponse(user.getEmail(), positionResponses);
	}
}
