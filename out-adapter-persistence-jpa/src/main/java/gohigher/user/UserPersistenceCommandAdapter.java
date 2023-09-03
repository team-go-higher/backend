package gohigher.user;

import org.springframework.stereotype.Component;

import gohigher.port.out.UserPersistenceCommandPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserPersistenceCommandAdapter implements UserPersistenceCommandPort {

	private final UserRepository userRepository;

	@Override
	public User save(User user) {
		final UserJpaEntity savedUser = userRepository.save(UserJpaEntity.from(user));
		return savedUser.convert();
	}
}
