package gohigher.user;

import java.util.ArrayList;
import java.util.List;

import gohigher.position.Position;
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
	private final List<Position> desiredPositions;

	public static User joinAsGuest(String email, Provider provider) {
		return new User(email, Role.GUEST, provider, new ArrayList<>());
	}
}
