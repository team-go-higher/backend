package gohigher.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.port.in.OAuth2CommandPort;
import gohigher.port.out.OauthLoginOutPort;
import gohigher.user.Role;
import gohigher.user.User;
import gohigher.user.oauth2.Provider;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OauthLoginInUseCase implements OAuth2CommandPort {

	private final OauthLoginOutPort oauthLoginOutPort;

	@Override
	public User login(final String email, final Provider provider) {
		return oauthLoginOutPort.findByEmail(email)
			.orElseGet(() -> oauthLoginOutPort.save(new User(email, Role.GUEST, provider)));
	}
}
