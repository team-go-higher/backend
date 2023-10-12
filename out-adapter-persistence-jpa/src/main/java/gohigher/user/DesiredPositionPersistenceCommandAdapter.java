package gohigher.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import gohigher.global.exception.GoHigherException;
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
	public void saveDesiredPositions(Long userId, Long mainPositionId, List<Long> positionIds) {
		UserJpaEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new GoHigherException(UserErrorCode.USER_NOT_EXISTS));

		savePositions(positionIds, user);
		assignMainPosition(mainPositionId, user);
	}

	private void savePositions(List<Long> positionIds, UserJpaEntity user) {
		List<PositionJpaEntity> subPositions = positionRepository.findAllById(positionIds);
		desiredPositionRepository.saveAll(subPositions.stream()
			.map(position -> new DesiredPositionJpaEntity(user, position, false))
			.collect(Collectors.toList()));
	}

	private void assignMainPosition(Long mainPositionId, UserJpaEntity user) {
		DesiredPositionJpaEntity desiredPosition = desiredPositionRepository.findByUserIdAndPositionId(
			user.getId(), mainPositionId)
			.orElseThrow(() -> new GoHigherException(UserErrorCode.DESIRED_POSITION_NOT_EXISTS));
		desiredPosition.assignMain();
	}
}
