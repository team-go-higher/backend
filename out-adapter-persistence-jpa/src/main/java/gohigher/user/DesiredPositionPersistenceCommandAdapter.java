package gohigher.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import gohigher.global.exception.GoHigherException;
import gohigher.position.PositionErrorCode;
import gohigher.position.entity.PositionJpaEntity;
import gohigher.position.entity.PositionRepository;
import gohigher.user.entity.DesiredPositionJpaEntity;
import gohigher.user.entity.DesiredPositionRepository;
import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;
import gohigher.user.port.out.DesiredPositionPersistenceCommandPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DesiredPositionPersistenceCommandAdapter implements DesiredPositionPersistenceCommandPort {

	private final UserRepository userRepository;
	private final PositionRepository positionRepository;
	private final DesiredPositionRepository desiredPositionRepository;

	@Override
	public void saveDesiredPositions(Long userId, Long mainPositionId, List<Long> subPositionIds) {
		UserJpaEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new GoHigherException(UserErrorCode.USER_NOT_EXISTS));

		PositionJpaEntity mainPosition = positionRepository.findById(mainPositionId)
			.orElseThrow(() -> new GoHigherException(PositionErrorCode.POSITION_NOT_EXISTS));
		List<PositionJpaEntity> subPositions = positionRepository.findAllById(subPositionIds);

		desiredPositionRepository.saveAll(makeDesiredPositions(user, mainPosition, subPositions));
	}

	private List<DesiredPositionJpaEntity> makeDesiredPositions(UserJpaEntity user, PositionJpaEntity mainPosition,
		List<PositionJpaEntity> subPositions) {
		DesiredPositionJpaEntity mainDesiredPosition = new DesiredPositionJpaEntity(user, mainPosition, true);
		List<DesiredPositionJpaEntity> desiredPositions = subPositions.stream()
			.map(position -> new DesiredPositionJpaEntity(user, position, false))
			.collect(Collectors.toList());

		desiredPositions.add(mainDesiredPosition);
		return desiredPositions;
	}
}
