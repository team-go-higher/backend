package gohigher.fixtureConverter;

import gohigher.position.Position;
import gohigher.position.entity.PositionJpaEntity;

public class PositionFixtureConverter {

	public static PositionJpaEntity convertToPositionEntity(Position position) {
		return new PositionJpaEntity(position.getValue());
	}
}
