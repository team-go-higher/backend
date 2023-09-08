package gohigher.application.port.out.persistence;

import java.util.Optional;

import gohigher.application.Application;

public interface ApplicationPersistenceQueryPort {

	Optional<Application> findById(Long id);
}
