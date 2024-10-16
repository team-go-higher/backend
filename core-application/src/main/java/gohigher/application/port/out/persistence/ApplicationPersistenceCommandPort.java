package gohigher.application.port.out.persistence;

import gohigher.application.Application;

public interface ApplicationPersistenceCommandPort {

	Application save(Long userId, Application application);

	void updateCurrentProcessOrder(long id, long userId, long processId);

	void updateSimply(Long processId, Application application);

	void delete(long applicationId);

	void updateSpecifically(Application updatedTarget);

	void updateCompleted(Application application);
}
