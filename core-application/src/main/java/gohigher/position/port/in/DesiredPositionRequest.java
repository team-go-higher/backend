package gohigher.position.port.in;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DesiredPositionRequest {

	private List<Long> existedPositionIds;
	private List<String> personalPositions;
}

