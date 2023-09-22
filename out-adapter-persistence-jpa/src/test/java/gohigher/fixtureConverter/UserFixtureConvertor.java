package gohigher.fixtureConverter;

import gohigher.user.User;
import gohigher.user.entity.UserJpaEntity;

public class UserFixtureConvertor {

	public static UserJpaEntity convertToUserEntity(User user) {
		return new UserJpaEntity(user.getEmail(), user.getRole(), user.getProvider());
	}
}
