package gohigher.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.global.exception.GoHigherException;
import gohigher.user.User;
import gohigher.user.UserErrorCode;
import gohigher.user.port.in.MyInfoResponse;
import gohigher.user.port.in.UserQueryPort;
import gohigher.user.port.out.UserPersistenceQueryPort;
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

	@Override
	public MyInfoResponse findMyInfo(Long id) {
		User loginUser = findById(id);
		return MyInfoResponse.from(loginUser);
	}
}
