package gohigher.position.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.global.exception.GoHigherException;
import gohigher.position.Position;
import gohigher.position.PositionErrorCode;
import gohigher.position.port.in.PositionCommandPort;
import gohigher.position.port.out.PositionPersistenceCommandPort;
import gohigher.position.port.out.PositionPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PositionCommandService implements PositionCommandPort {

	private final PositionPersistenceCommandPort positionPersistenceCommandPort;
	private final PositionPersistenceQueryPort positionPersistenceQueryPort;

	@Override
	public List<Long> savePersonalPositions(Long userId, List<String> positions) {
		validateAlreadyExistedPosition(positions);

		List<Position> personalPositions = positions.stream()
			.map(Position::new)
			.toList();

		return positionPersistenceCommandPort.saveAll(personalPositions);
	}

	private void validateAlreadyExistedPosition(List<String> positions) {
		if (positionPersistenceQueryPort.existsByValues(positions)) {
			throw new GoHigherException(PositionErrorCode.EXIST_ALREADY_POSITION);
		}
	}
}
