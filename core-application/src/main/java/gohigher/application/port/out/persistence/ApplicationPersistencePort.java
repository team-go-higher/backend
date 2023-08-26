package gohigher.application.port.out.persistence;

import gohigher.application.Application;

public interface ApplicationPersistencePort {
	Long save(Long userId, Application application);
}