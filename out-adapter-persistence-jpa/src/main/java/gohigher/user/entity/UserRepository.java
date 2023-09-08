package gohigher.user.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

	Optional<UserJpaEntity> findByEmail(String email);
}
