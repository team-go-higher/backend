package gohigher.port.in;

import gohigher.user.User;
import gohigher.user.oauth2.Provider;

public interface OAuth2CommandPort {

	User login(String email, Provider provider);
}
