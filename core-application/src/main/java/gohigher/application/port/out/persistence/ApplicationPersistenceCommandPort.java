package gohigher.application.port.out.persistence;

import gohigher.application.Application;

public interface ApplicationPersistenceCommandPort {

	Long save(Application application);
}
