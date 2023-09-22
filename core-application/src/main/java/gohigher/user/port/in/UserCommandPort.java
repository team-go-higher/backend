package gohigher.user.port.in;

import gohigher.user.User;
import gohigher.user.auth.Provider;

public interface UserCommandPort {

	User signIn(String email, Provider provider);
}
