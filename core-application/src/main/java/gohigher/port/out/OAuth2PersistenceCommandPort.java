package gohigher.port.out;

import gohigher.user.User;

public interface OAuth2PersistenceCommandPort {

	User save(User user);
}
