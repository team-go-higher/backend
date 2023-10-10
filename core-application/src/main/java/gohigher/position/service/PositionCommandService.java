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

	private final PositionPersistenceQueryPort positionPersistenceQueryPort;
	private final DesiredPositionPersistenceCommandPort desiredPositionPersistenceCommandPort;

	@Override
	public void saveDesiredPositions(Long userId, List<Long> positionIds) {
		List<Long> distinctPositionIds = positionIds.stream()
			.distinct()
			.collect(Collectors.toList());
		validateDuplicatedPositionIds(positionIds, distinctPositionIds);
		validateExistedPositions(distinctPositionIds);
		desiredPositionPersistenceCommandPort.saveDesiredPositions(userId, distinctPositionIds);
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
}
