package gohigher.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

<<<<<<< HEAD
	Optional<UserJpaEntity> findByEmail(final String email);
=======
	Optional<UserJpaEntity> findByEmail(String email);
>>>>>>> d02a314 ([FEATURE] Spring Security 적용 (#10))
}
