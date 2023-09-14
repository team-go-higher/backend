package gohigher.application.port.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import gohigher.application.Application;

public interface ApplicationPersistenceQueryPort {

	Optional<Application> findById(Long id);

	List<Application> findByUserIdAndMonth(Long userId, int year, int month);

	List<Application> findByUserIdAndDate(Long userId, LocalDate date);
}
