package gohigher.position.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.global.exception.GoHigherException;
import gohigher.position.Position;
import gohigher.position.PositionErrorCode;
import gohigher.position.port.in.PositionCommandPort;
import gohigher.position.port.out.DesiredPositionPersistenceCommandPort;
import gohigher.position.port.out.PositionPersistenceCommandPort;
import gohigher.position.port.out.PositionPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PositionCommandService implements PositionCommandPort {

	private final PositionPersistenceCommandPort positionPersistenceCommandPort;
	private final PositionPersistenceQueryPort positionPersistenceQueryPort;
	private final DesiredPositionPersistenceCommandPort desiredPositionPersistenceCommandPort;

	@Override
	public List<Long> savePersonalPositions(Long userId, List<String> positions) {
		validateAlreadyExistedPosition(positions);

		List<Position> personalPositions = positions.stream()
			.distinct()
			.map(Position::new)
			.toList();

		return positionPersistenceCommandPort.saveAll(personalPositions);
	}

	@Override
	public void saveDesiredPositions(Long userId, List<Long> positionIds) {
		desiredPositionPersistenceCommandPort.saveDesiredPositions(userId, positionIds);
	}

	private void validateAlreadyExistedPosition(List<String> positions) {
		if (positionPersistenceQueryPort.existsByValuesAndMadeByAdmin(positions)) {
			throw new GoHigherException(PositionErrorCode.EXIST_ALREADY_POSITION);
		}
	}
}
