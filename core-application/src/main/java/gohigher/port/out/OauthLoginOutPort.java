package gohigher.port.out;

import java.util.Optional;

import gohigher.user.User;

public interface OauthLoginOutPort {

	Optional<User> findByEmail(final String email);

	User save(final User user);
}
