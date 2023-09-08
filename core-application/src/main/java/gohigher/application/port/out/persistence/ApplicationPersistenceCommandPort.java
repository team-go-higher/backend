package gohigher.application.port.out.persistence;

import gohigher.application.Application;
import gohigher.common.Process;

public interface ApplicationPersistenceCommandPort {

	Long save(Application application);

	void updateCurrentProcess(Long id, Process process);
}
