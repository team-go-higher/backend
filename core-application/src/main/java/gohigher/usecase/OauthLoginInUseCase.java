package gohigher.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.port.in.OauthLoginInPort;
import gohigher.port.out.OauthLoginOutPort;
import gohigher.user.Provider;
import gohigher.user.Role;
import gohigher.user.User;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OauthLoginInUseCase implements OauthLoginInPort {

	private final OauthLoginOutPort userRepository;

	@Override
	public User login(final String email, final Provider provider) {
		return userRepository.findByEmail(email)
			.orElseGet(() -> userRepository.save(new User(email, Role.GUEST, provider)));
	}
}
