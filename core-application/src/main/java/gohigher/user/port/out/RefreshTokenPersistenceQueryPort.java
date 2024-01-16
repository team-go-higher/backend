package gohigher.user.port.out;

import java.util.Optional;

public interface RefreshTokenPersistenceQueryPort {

	Optional<String> findByUserId(Long userId);
}
