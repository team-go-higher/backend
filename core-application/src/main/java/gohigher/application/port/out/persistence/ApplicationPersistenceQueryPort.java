package gohigher.application.port.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import gohigher.application.Application;
import gohigher.common.ProcessType;
import gohigher.pagination.PagingContainer;

public interface ApplicationPersistenceQueryPort {

	boolean existsByIdAndUserId(Long id, Long userId);

	Optional<Application> findByIdAndUserId(Long id, Long userId);

	List<Application> findByUserIdAndMonth(Long userId, int year, int month);

	List<Application> findByUserIdAndDate(Long userId, LocalDate date);

	PagingContainer<Application> findUnscheduledByUserId(Long userId, int page, int size);

	List<Application> findOnlyCurrentProcessByUserId(Long userId);

	PagingContainer<Application> findOnlyCurrentProcessByUserIdAndProcessType(Long userId, ProcessType processType, int page, int size);
}
