package gohigher.position;

import java.util.List;

import org.springframework.stereotype.Component;

import gohigher.position.entity.PositionJpaEntity;
import gohigher.position.entity.PositionRepository;
import gohigher.position.port.out.PositionPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PositionPersistenceQueryAdapter implements PositionPersistenceQueryPort {

	private final PositionRepository positionRepository;

	@Override
	public List<Position> findAll() {
		return positionRepository.findAll()
			.stream()
			.map(PositionJpaEntity::toDomain)
			.toList();
	}

	@Override
	public boolean existsByIds(List<Long> positionIds) {
		return positionRepository.existsAllByIdIn(positionIds);
	}
}
