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
	public User findById(Long id) {
		return userPersistenceQueryPort.findById(id)
			.orElseThrow(() -> new GoHigherException(UserErrorCode.USER_NOT_EXISTS));
	}
}
