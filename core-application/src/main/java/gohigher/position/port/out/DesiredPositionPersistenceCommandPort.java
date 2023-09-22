package gohigher.position.port.out;

import java.util.List;

public interface DesiredPositionPersistenceCommandPort {

	void saveDesiredPositions(Long userId, List<Long> positionIds);
}
