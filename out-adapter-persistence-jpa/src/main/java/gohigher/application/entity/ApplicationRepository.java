package gohigher.application.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<ApplicationJpaEntity, Long> {

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "JOIN FETCH a.processes p "
		+ "WHERE a.userId = :userId "
		+ "AND FUNCTION('YEAR', p.schedule) = :year "
		+ "AND FUNCTION('MONTH', p.schedule) = :month")
	List<ApplicationJpaEntity> findByUserIdAndDate(Long userId, int year, int month);
}
