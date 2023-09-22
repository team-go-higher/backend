package gohigher.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;
import gohigher.user.port.out.UserPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserPersistenceQueryAdapter implements UserPersistenceQueryPort {

	private final UserRepository userRepository;

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email)
			.map(UserJpaEntity::toDomain);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id)
			.map(UserJpaEntity::toDomain);
	}
}
