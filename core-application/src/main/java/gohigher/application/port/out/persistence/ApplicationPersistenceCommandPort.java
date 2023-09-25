package gohigher.application.port.out.persistence;

import gohigher.application.Application;

public interface ApplicationPersistenceCommandPort {

	Long save(Long userId, Application application);

	void updateCurrentProcessOrder(long id, long processId);

	void update(Application application);
}
