package gohigher.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {

	private final String email;
	private final Role role;
	private final Provider provider;
	private Long id;

	public User(String email, Role role, Provider provider) {
		this.email = email;
		this.role = role;
		this.provider = provider;
	}
}
