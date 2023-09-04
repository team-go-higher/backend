package gohigher.user;

import gohigher.user.auth.Provider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class User {

	private final String email;
	private final Role role;
	private final Provider provider;
	private Long id;

	public User(Long id, String email, Role role, Provider provider) {
		this.id = id;
		this.email = email;
		this.role = role;
		this.provider = provider;
	}
}
