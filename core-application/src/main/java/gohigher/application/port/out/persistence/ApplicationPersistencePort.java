package gohigher.application.port.out.persistence;

import gohigher.application.Application;

public interface ApplicationPersistencePort {
	void save(Long userId, Application application);
}