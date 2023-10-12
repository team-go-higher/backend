package gohigher.user.port.out;

import java.util.List;

public interface DesiredPositionPersistenceCommandPort {

	void saveDesiredPositions(Long userId, Long mainPositionId, List<Long> positionIds);
}
