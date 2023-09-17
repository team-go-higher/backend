package gohigher.application.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gohigher.common.ProcessType;

public interface ApplicationProcessRepository extends JpaRepository<ApplicationProcessJpaEntity, Long> {

	@Query("SELECT ap FROM ApplicationProcessJpaEntity ap "
		+ "WHERE ap.application.id = :applicationId "
		+ "AND ap.type = :type "
		+ "ORDER BY ap.order")
	List<ApplicationProcessJpaEntity> findByApplicationIdAndType(Long applicationId, ProcessType type);
}
