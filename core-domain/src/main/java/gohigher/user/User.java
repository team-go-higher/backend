package gohigher.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class User {

	private Long id;
	private final String email;
	private final Role role;
	private final Provider provider;

	public User(final Long id, final String email, final Role role, final Provider provider) {
		this.id = id;
		this.email = email;
		this.role = role;
		this.provider = provider;
	}
}
