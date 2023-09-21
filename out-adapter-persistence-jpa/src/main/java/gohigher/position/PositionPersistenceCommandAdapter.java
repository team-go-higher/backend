package gohigher.position;

import java.util.List;

import org.springframework.stereotype.Component;

import gohigher.position.entity.PositionJpaEntity;
import gohigher.position.entity.PositionRepository;
import gohigher.position.port.out.PositionPersistenceCommandPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PositionPersistenceCommandAdapter implements PositionPersistenceCommandPort {

	private final PositionRepository positionRepository;

	@Override
	public List<Long> saveAll(List<Position> positions) {
		List<PositionJpaEntity> positionJpaEntities = positions.stream()
			.map(position -> new PositionJpaEntity(position.getValue(), false))
			.toList();

		return positionRepository.saveAll(positionJpaEntities)
			.stream()
			.map(PositionJpaEntity::getId)
			.toList();
	}
}
