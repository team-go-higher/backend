package gohigher.position;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Position {

	private final String value;
	private final List<SpecificPosition> specificPositions;
}
