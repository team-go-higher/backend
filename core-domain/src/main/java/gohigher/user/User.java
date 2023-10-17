package gohigher.user;

import gohigher.user.auth.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class User {

	private Long id;
	private final String email;
	private final Role role;
	private final Provider provider;
	private final DesiredPositions desiredPositions;

	public static User joinAsGuest(String email, Provider provider) {
		return new User(email, Role.GUEST, provider, DesiredPositions.initializeForGuest());
	}

	public boolean hasRole(Role role) {
		return this.role.equals(role);
	}

	public User changeRole(Role role) {
		return new User(this.id, this.email, role, this.provider, this.desiredPositions);
	}
}
