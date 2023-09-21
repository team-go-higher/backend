package gohigher.application.port.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import gohigher.application.Application;
import gohigher.pagination.PagingParameters;
import gohigher.pagination.SliceContainer;

public interface ApplicationPersistenceQueryPort {

	boolean existsByIdAndUserId(Long id, Long userId);

	Optional<Application> findByIdAndUserId(Long id, Long userId);

	List<Application> findByUserIdAndMonth(Long userId, int year, int month);

	List<Application> findByUserIdAndDate(Long userId, LocalDate date);

	SliceContainer<Application> findByUserIdWithoutSchedule(Long userId, PagingParameters pagingParameters);

	List<Application> findOnlyWithCurrentProcessByUserId(Long userId);
}
