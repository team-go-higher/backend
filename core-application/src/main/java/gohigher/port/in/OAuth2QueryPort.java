package gohigher.port.in;

import gohigher.user.User;

public interface OAuth2QueryPort {

	User findByEmail(final String email);
}
