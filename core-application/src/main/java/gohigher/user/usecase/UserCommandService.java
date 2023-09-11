package gohigher.user.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.user.User;
import gohigher.user.auth.Provider;
import gohigher.user.port.in.UserCommandPort;
import gohigher.user.port.out.UserPersistenceCommandPort;
import gohigher.user.port.out.UserPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService implements UserCommandPort {

	private final UserPersistenceQueryPort userPersistenceQueryPort;
	private final UserPersistenceCommandPort userPersistenceCommandPort;

	@Override
	public User login(String email, Provider provider) {
		return userPersistenceQueryPort.findByEmail(email)
			.orElseGet(() -> userPersistenceCommandPort.save(User.join(email, provider)));
	}
}
