package gohigher.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.port.in.OAuth2CommandPort;
import gohigher.port.out.OAuth2PersistenceCommandPort;
import gohigher.port.out.OAuth2PersistenceQueryPort;
import gohigher.user.Role;
import gohigher.user.User;
import gohigher.user.oauth2.Provider;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2CommandService implements OAuth2CommandPort {

	private final OAuth2PersistenceQueryPort oAuth2PersistenceQueryPort;
	private final OAuth2PersistenceCommandPort oAuth2PersistenceCommandPort;

	@Override
	public User login(String email, Provider provider) {
		return oAuth2PersistenceQueryPort.findByEmail(email)
			.orElseGet(() -> oAuth2PersistenceCommandPort.save(new User(email, Role.GUEST, provider)));
	}
}
