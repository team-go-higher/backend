package gohigher.user;

import org.springframework.stereotype.Component;

import gohigher.global.exception.GoHigherException;
import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;
import gohigher.user.port.out.UserPersistenceCommandPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserPersistenceCommandAdapter implements UserPersistenceCommandPort {

	private final UserRepository userRepository;

	@Override
	public User save(User user) {
		final UserJpaEntity savedUser = userRepository.save(UserJpaEntity.from(user));
		return savedUser.toDomain();
	}

	@Override
	public void updateRole(User user) {
		UserJpaEntity userEntity = userRepository.findById(user.getId())
			.orElseThrow(() -> new GoHigherException(UserErrorCode.USER_NOT_EXISTS));

		userEntity.updateRole(user.getRole());
	}
}
