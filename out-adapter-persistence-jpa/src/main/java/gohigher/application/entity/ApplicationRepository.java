package gohigher.application.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<ApplicationJpaEntity, Long> {

	boolean existsByIdAndUserId(Long id, Long userId);

	@Modifying
	@Query("UPDATE ApplicationJpaEntity a "
		+ "SET a.currentProcessOrder = "
		+ "(SELECT ap.order FROM ApplicationProcessJpaEntity ap WHERE ap.id = :processId) "
		+ "WHERE a.id = :id")
	void updateCurrentProcessOrder(long id, long processId);

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "JOIN FETCH a.processes p "
		+ "WHERE a.userId = :userId "
		+ "AND FUNCTION('YEAR', p.schedule) = :year "
		+ "AND FUNCTION('MONTH', p.schedule) = :month")
	List<ApplicationJpaEntity> findByUserIdAndMonth(Long userId, int year, int month);

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "JOIN FETCH a.processes p "
		+ "WHERE a.userId = :userId "
		+ "AND p.schedule >= :startOfDate "
		+ "AND p.schedule < :endOfDate")
	List<ApplicationJpaEntity> findByUserIdAndDate(Long userId, LocalDateTime startOfDate, LocalDateTime endOfDate);
}
