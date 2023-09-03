package gohigher.port.out;

import java.util.Optional;

import gohigher.user.User;

public interface OAuth2PersistenceQueryPort {

	Optional<User> findByEmail(String email);
}
