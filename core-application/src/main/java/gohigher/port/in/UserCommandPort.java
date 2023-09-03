package gohigher.port.in;

import gohigher.user.User;
import gohigher.user.oauth2.Provider;

public interface UserCommandPort {

	User login(String email, Provider provider);
}
