package gohigher.application.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationProcessRepository extends JpaRepository<ApplicationProcessJpaEntity, Long> {

	Optional<ApplicationProcessJpaEntity> findById(Long id);
}
