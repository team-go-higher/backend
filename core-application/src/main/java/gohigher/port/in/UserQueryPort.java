package gohigher.port.in;

import gohigher.user.User;

public interface UserQueryPort {

	User findById(Long id);
}
