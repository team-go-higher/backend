package gohigher.user;

import gohigher.user.oauth2.Provider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class User {

	private Long id;
	private final String email;
	private final Role role;
	private final Provider provider;

	public User(Long id, String email, Role role, Provider provider) {
		this.id = id;
		this.email = email;
		this.role = role;
		this.provider = provider;
	}
}
