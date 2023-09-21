package gohigher.position.port.in;

import java.util.List;

public interface PositionCommandPort {

	List<Long> savePersonalPositions(Long userId, List<String> positions);
}
