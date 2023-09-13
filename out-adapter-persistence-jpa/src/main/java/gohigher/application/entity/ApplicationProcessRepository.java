package gohigher.application.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationProcessRepository extends JpaRepository<ApplicationProcessJpaEntity, Long> {

	@Query("SELECT ap FROM ApplicationProcessJpaEntity ap "
		+ "WHERE ap.id = :id AND "
		+ "ap.application.id = :applicationId")
	Optional<ApplicationProcessJpaEntity> findByIdAndApplicationId(Long id, Long applicationId);
}
