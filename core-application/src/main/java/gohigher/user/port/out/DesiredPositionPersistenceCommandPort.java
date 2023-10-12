package gohigher.user.port.out;

import java.util.List;

public interface DesiredPositionPersistenceCommandPort {

	void saveDesiredPositions(Long userId, List<Long> positionIds);

	void assignMainPosition(Long userId, Long mainPositionId);
}
