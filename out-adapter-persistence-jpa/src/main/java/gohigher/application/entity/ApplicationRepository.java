package gohigher.application.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import gohigher.application.dto.CurrentProcessDto;

public interface ApplicationRepository extends JpaRepository<ApplicationJpaEntity, Long> {

	boolean existsByIdAndUserId(Long id, Long userId);

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "LEFT JOIN FETCH a.processes p "
		+ "WHERE a.id = :id "
		+ "AND a.userId = :userId "
		+ "AND a.deleted = false")
	Optional<ApplicationJpaEntity> findByIdAndUserIdWithProcess(Long id, Long userId);

	@Modifying
	@Query("UPDATE ApplicationJpaEntity a "
		+ "SET a.currentProcessOrder = "
		+ "(SELECT ap.order FROM ApplicationProcessJpaEntity ap WHERE ap.id = :processId) "
		+ "WHERE a.id = :id")
	void updateCurrentProcessOrder(long id, long processId);

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "JOIN FETCH a.processes p "
		+ "WHERE a.userId = :userId "
		+ "AND a.deleted = false "
		+ "AND FUNCTION('YEAR', p.schedule) = :year "
		+ "AND FUNCTION('MONTH', p.schedule) = :month")
	List<ApplicationJpaEntity> findByUserIdAndMonth(Long userId, int year, int month);

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "JOIN FETCH a.processes p "
		+ "WHERE a.userId = :userId "
		+ "AND a.deleted = false "
		+ "AND p.schedule >= :startOfDate "
		+ "AND p.schedule < :endOfDate")
	List<ApplicationJpaEntity> findByUserIdAndDate(Long userId, LocalDateTime startOfDate, LocalDateTime endOfDate);

	@Query(
		value = "SELECT * "
			+ "FROM application AS a "
			+ "LEFT JOIN application_process AS p "
			+ "ON a.current_process = p.orders "
			+ "WHERE a.user_id = ?1 "
			+ "AND a.deleted = false",
		countQuery = "SELECT count(a.id) "
			+ "FROM application AS a "
			+ "LEFT JOIN application_process AS p "
			+ "ON a.current_process = p.orders "
			+ "WHERE a.user_id = ?1 "
			+ "AND a.deleted = false",
		nativeQuery = true
	)
	List<CurrentProcessDto> findCurrentProcessByUserId(Long userId);
}
