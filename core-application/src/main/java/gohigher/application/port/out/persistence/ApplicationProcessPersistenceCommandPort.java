package gohigher.application.port.out.persistence;

import gohigher.common.Process;

public interface ApplicationProcessPersistenceCommandPort {

	Process save(long applicationId, Process process);
}
