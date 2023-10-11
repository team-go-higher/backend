package gohigher.position.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.global.exception.GoHigherException;
import gohigher.position.PositionErrorCode;
import gohigher.position.port.in.PositionCommandPort;
import gohigher.position.port.out.DesiredPositionPersistenceCommandPort;
import gohigher.position.port.out.PositionPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PositionCommandService implements PositionCommandPort {

	private static final int MAIN_POSITION_IDX = 0;

	private final PositionPersistenceQueryPort positionPersistenceQueryPort;
	private final DesiredPositionPersistenceCommandPort desiredPositionPersistenceCommandPort;

	@Override
	public void saveDesiredPositions(Long userId, List<Long> positionIds) {
		List<Long> distinctPositionIds = positionIds.stream()
			.distinct()
			.collect(Collectors.toList());
		validateDuplicatedPositionIds(positionIds, distinctPositionIds);
		validateExistedPositions(distinctPositionIds);

		Long mainDesiredPositionId = extractMainPositionId(positionIds);
		desiredPositionPersistenceCommandPort.saveDesiredPositions(userId, mainDesiredPositionId, distinctPositionIds);
	}

	private void validateDuplicatedPositionIds(List<Long> positionIds, List<Long> distinctPositionIds) {
		if (distinctPositionIds.size() != positionIds.size()) {
			throw new GoHigherException(PositionErrorCode.DUPLICATED_POSITION);
		}
	}

	private void validateExistedPositions(List<Long> positions) {
		if (!positionPersistenceQueryPort.existsByIds(positions)) {
			throw new GoHigherException(PositionErrorCode.POSITION_NOT_EXISTS);
		}
	}

	private Long extractMainPositionId(List<Long> positionIds) {
		if (positionIds.isEmpty()) {
			throw new GoHigherException(PositionErrorCode.POSITION_NOT_EXISTS);
		}

		return positionIds.remove(MAIN_POSITION_IDX);
	}
}
