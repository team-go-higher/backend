package gohigher.port.in;

import gohigher.user.User;
import gohigher.user.auth.Provider;

public interface UserCommandPort {

	User login(String email, Provider provider);
}