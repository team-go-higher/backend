package gohigher.position;

import java.util.List;

import org.springframework.stereotype.Component;

import gohigher.global.exception.GoHigherException;
import gohigher.position.entity.DesiredPositionJpaEntity;
import gohigher.position.entity.DesiredPositionRepository;
import gohigher.position.entity.PositionJpaEntity;
import gohigher.position.entity.PositionRepository;
import gohigher.position.port.out.DesiredPositionPersistenceCommandPort;
import gohigher.user.UserErrorCode;
import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DesiredPositionPersistenceCommandAdapter implements DesiredPositionPersistenceCommandPort {

	private final UserRepository userRepository;
	private final PositionRepository positionRepository;
	private final DesiredPositionRepository desiredPositionRepository;

	@Override
	public void saveDesiredPositions(Long userId, List<Long> positionIds) {
		UserJpaEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new GoHigherException(UserErrorCode.USER_NOT_EXISTS));

		List<PositionJpaEntity> positions = positionRepository.findAllById(positionIds);

		List<DesiredPositionJpaEntity> desiredPositions = positions.stream()
			.map(position -> new DesiredPositionJpaEntity(user, position))
			.toList();
		desiredPositionRepository.saveAll(desiredPositions);
	}
}
