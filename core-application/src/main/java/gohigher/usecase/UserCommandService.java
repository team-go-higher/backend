package gohigher.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.port.in.UserCommandPort;
import gohigher.port.out.UserPersistenceCommandPort;
import gohigher.port.out.UserPersistenceQueryPort;
import gohigher.user.Role;
import gohigher.user.User;
import gohigher.user.oauth2.Provider;
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
			.orElseGet(() -> userPersistenceCommandPort.save(new User(email, Role.GUEST, provider)));
	}
}
