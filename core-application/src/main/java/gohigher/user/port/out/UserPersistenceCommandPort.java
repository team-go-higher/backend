package gohigher.user.port.out;

import gohigher.user.Role;
import gohigher.user.User;

public interface UserPersistenceCommandPort {

	User save(User user);

	void updateRole(Long userId, Role role);
}
