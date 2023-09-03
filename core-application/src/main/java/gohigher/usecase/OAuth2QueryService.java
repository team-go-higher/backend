package gohigher.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.port.in.OAuth2QueryPort;
import gohigher.port.out.OAuth2PersistenceQueryPort;
import gohigher.user.User;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuth2QueryService implements OAuth2QueryPort {

	private final OAuth2PersistenceQueryPort oAuth2PersistenceQueryPort;

	@Override
	public User findByEmail(String email) {
		return oAuth2PersistenceQueryPort.findByEmail(email)
			.orElseThrow(IllegalAccessError::new);
	}

}
