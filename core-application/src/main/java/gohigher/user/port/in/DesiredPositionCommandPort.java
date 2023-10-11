package gohigher.user.port.in;

import java.util.List;

public interface DesiredPositionCommandPort {

	void saveDesiredPositions(Long userId, List<Long> positionIds);
}
