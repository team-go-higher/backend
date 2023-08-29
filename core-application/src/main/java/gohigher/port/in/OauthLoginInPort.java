package gohigher.port.in;

import gohigher.user.User;
import gohigher.user.oauth2.Provider;

public interface OauthLoginInPort {

	User login(final String email, final Provider provider);
}
