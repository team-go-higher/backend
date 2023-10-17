package gohigher.user;

import java.util.ArrayList;
import java.util.List;

import gohigher.position.Position;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DesiredPositions {

	private final Position mainPosition;
	private final List<Position> subPositions;

	public static DesiredPositions initializeForGuest() {
		return new DesiredPositions(null, new ArrayList<>());
	}
}
