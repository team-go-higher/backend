package gohigher.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

	Optional<UserJpaEntity> findByEmail(String email);
}
