package gohigher.port.out;

import java.util.Optional;

import gohigher.user.User;

public interface UserPersistenceQueryPort {

	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);
}
