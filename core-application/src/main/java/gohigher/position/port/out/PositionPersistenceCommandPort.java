package gohigher.position.port.out;

import java.util.List;

import gohigher.position.Position;

public interface PositionPersistenceCommandPort {

	List<Long> saveAll(Long userId, List<Position> positions);
}
