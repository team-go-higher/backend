package gohigher.position.port.in;

import java.util.List;

public interface PositionQueryPort {

	List<PositionResponse> findPositions();
}
