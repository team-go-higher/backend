package gohigher.application.port.out.persistence;

import java.util.Optional;
import java.util.List;

import gohigher.application.Application;

public interface ApplicationPersistenceQueryPort {

	Optional<Application> findById(Long id);

	List<Application> findByIdAndMonth(Long userId, int year, int month);
}
