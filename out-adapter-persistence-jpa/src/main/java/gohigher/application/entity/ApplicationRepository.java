package gohigher.application.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gohigher.common.ProcessType;

public interface ApplicationRepository extends JpaRepository<ApplicationJpaEntity, Long> {

	boolean existsByIdAndUserId(Long id, Long userId);

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "LEFT JOIN FETCH a.processes p "
		+ "WHERE a.id = :id "
		+ "AND a.userId = :userId "
		+ "AND a.deleted = false")
	Optional<ApplicationJpaEntity> findByIdAndUserIdWithProcess(Long id, Long userId);

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

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "JOIN FETCH a.processes p "
		+ "WHERE a.userId = :userId "
		+ "AND p.isCurrent = true "
		+ "AND p.schedule = null "
		+ "AND a.deleted = false")
	Slice<ApplicationJpaEntity> findUnscheduledByUserId(Long userId, Pageable pageable);

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "JOIN FETCH a.processes p "
		+ "WHERE a.userId = :userId "
		+ "AND p.isCurrent = true "
		+ "AND a.deleted = false")
	List<ApplicationJpaEntity> findOnlyWithCurrentProcessByUserId(Long userId);

	@Query("SELECT a FROM ApplicationJpaEntity a "
		+ "JOIN FETCH a.processes p "
		+ "WHERE a.userId = :userId "
		+ "AND p.isCurrent = true "
		+ "AND p.type = :processType "
		+ "AND a.deleted = false")
	List<ApplicationJpaEntity> findOnlyCurrentProcessByUserIdAndProcessType(Long userId, ProcessType processType);
}
