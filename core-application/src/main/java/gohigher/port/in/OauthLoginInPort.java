package gohigher.port.in;

import gohigher.user.Provider;
import gohigher.user.User;

public interface OauthLoginInPort {

	User login(final String email, final Provider provider);
}
