package gohigher.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.global.exception.GoHigherException;
import gohigher.port.in.UserQueryPort;
import gohigher.port.out.UserPersistenceQueryPort;
import gohigher.user.User;
import gohigher.user.UserErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService implements UserQueryPort {

	private final UserPersistenceQueryPort userPersistenceQueryPort;

	@Override
	public User findByEmail(String email) {
		return userPersistenceQueryPort.findByEmail(email)
			.orElseThrow(() -> new GoHigherException(UserErrorCode.USER_NOT_EXISTS));
	}

}
