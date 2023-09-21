package gohigher.position.port.out;

import java.util.List;

import gohigher.position.Position;

public interface PositionPersistenceQueryPort {

	List<Position> findAll();
}
