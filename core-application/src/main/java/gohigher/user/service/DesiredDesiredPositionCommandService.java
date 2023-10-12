package gohigher.user.service;

import java.util.List;
import java.util.stream.Collectors;

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

	private static final int MAIN_POSITION_IDX = 0;

	private final PositionPersistenceQueryPort positionPersistenceQueryPort;
	private final DesiredPositionPersistenceCommandPort desiredPositionPersistenceCommandPort;

	@Override
	public void saveDesiredPositions(Long userId, List<Long> positionIds) {
		validateDuplicatedPositionIds(positionIds);
		validateExistedPositions(positionIds);

		Long mainDesiredPositionId = extractMainPositionId(positionIds);
		desiredPositionPersistenceCommandPort.saveDesiredPositions(userId, mainDesiredPositionId,
			positionIds);
	}

	private void validateDuplicatedPositionIds(List<Long> positionIds) {
		List<Long> distinctPositionIds = positionIds.stream()
			.distinct()
			.collect(Collectors.toList());

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
			throw new GoHigherException(PositionErrorCode.EMPTY_INPUT_POSITION_ID);
		}

		return positionIds.remove(MAIN_POSITION_IDX);
	}
}
