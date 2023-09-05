package gohigher.user.port.out;

import gohigher.user.User;

public interface UserPersistenceCommandPort {

	User save(User user);
}
