package gohigher.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

import gohigher.port.out.OauthLoginOutPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OauthLoginRepository implements OauthLoginOutPort {

	private final UserRepository userRepository;

	@Override
	public Optional<User> findByEmail(final String email) {
		return userRepository.findByEmail(email)
			.map(UserJpaEntity::convert);
	}

	@Override
	public User save(final User user) {
		final UserJpaEntity savedUser = userRepository.save(UserJpaEntity.from(user));
		return savedUser.convert();
	}
}
