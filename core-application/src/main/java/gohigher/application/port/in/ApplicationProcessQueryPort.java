package gohigher.application.port.in;

import java.util.List;

import gohigher.common.ProcessType;

public interface ApplicationProcessQueryPort {

	List<ApplicationProcessByProcessTypeResponse> findByApplicationIdAndProcessType(Long userId, Long applicationId,
		ProcessType processType);
}
