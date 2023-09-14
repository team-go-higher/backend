package gohigher.application.port.out.persistence;

import java.util.List;
import java.util.Optional;

import gohigher.application.Application;

public interface ApplicationPersistenceQueryPort {

	boolean existsByIdAndUserId(Long id, Long userId);

	List<Application> findByIdAndMonth(Long userId, int year, int month);

	Optional<Application> findByIdAndUserId(Long id, Long userId);
}
