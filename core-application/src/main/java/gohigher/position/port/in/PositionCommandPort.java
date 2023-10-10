package gohigher.position.port.in;

import java.util.List;

public interface PositionCommandPort {

	void saveDesiredPositions(Long userId, List<Long> positionIds);
}
