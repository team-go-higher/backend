package gohigher.application.port.out.persistence;

import java.util.List;

import gohigher.common.Process;
import gohigher.common.ProcessType;

public interface ApplicationProcessPersistenceQueryPort {

	boolean existsById(Long id);

	List<Process> findByApplicationIdAndProcessType(Long applicationId, ProcessType processType);
}
