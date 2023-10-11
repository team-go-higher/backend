package gohigher.user;

import gohigher.user.auth.Provider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserFixture {

	AZPI("azpi@email.com", Role.GUEST, Provider.GOOGLE),
	;

	private final String email;
	private final Role role;
	private final Provider provider;

	public User toDomain() {
		return new User(null, email, role, provider, DesiredPositions.initializeForGuest());
	}
}
