package gohigher.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.global.exception.GoHigherException;
import gohigher.position.PositionErrorCode;
import gohigher.position.port.out.PositionPersistenceQueryPort;
import gohigher.user.port.in.DesiredPositionCommandPort;
import gohigher.user.port.out.DesiredPositionPersistenceCommandPort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DesiredDesiredPositionCommandService implements DesiredPositionCommandPort {

	private final PositionPersistenceQueryPort positionPersistenceQueryPort;
	private final DesiredPositionPersistenceCommandPort desiredPositionPersistenceCommandPort;

	@Override
	public void saveDesiredPositions(Long userId, List<Long> positionIds) {
		validateDistinctPositionIds(positionIds);
		validateExistedPositions(positionIds);

		desiredPositionPersistenceCommandPort.saveDesiredPositions(userId, positionIds);
	}

	@Override
	public void assignMainDesiredPosition(Long userId, Long mainPositionId) {
		desiredPositionPersistenceCommandPort.assignMainPosition(userId, mainPositionId);
	}

	private void validateDistinctPositionIds(List<Long> positionIds) {
		List<Long> distinctPositionIds = positionIds.stream()
			.distinct()
			.toList();

		if (distinctPositionIds.size() != positionIds.size()) {
			throw new GoHigherException(PositionErrorCode.DUPLICATED_POSITION);
		}
	}

	private void validateExistedPositions(List<Long> positions) {
		if (!positionPersistenceQueryPort.existsByIds(positions)) {
			throw new GoHigherException(PositionErrorCode.POSITION_NOT_EXISTS);
		}
	}
}
