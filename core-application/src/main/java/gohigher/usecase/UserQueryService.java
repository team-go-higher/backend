package gohigher.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.port.in.UserQueryPort;
import gohigher.port.out.UserPersistenceQueryPort;
import gohigher.user.User;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService implements UserQueryPort {

	private final UserPersistenceQueryPort userPersistenceQueryPort;

	@Override
	public User findByEmail(String email) {
		return userPersistenceQueryPort.findByEmail(email)
			.orElseThrow(IllegalAccessError::new);
	}

}
