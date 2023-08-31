package gohigher.port.in;

import gohigher.user.Provider;
import gohigher.user.User;

public interface OAuth2CommandPort {

	User login(final String email, final Provider provider);
}
