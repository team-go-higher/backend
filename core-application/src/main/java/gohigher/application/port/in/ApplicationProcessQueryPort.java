package gohigher.application.port.in;

import java.util.List;

public interface ApplicationProcessQueryPort {

	List<ApplicationProcessByProcessTypeResponse> findByApplicationIdAndProcessType(
		Long userId, ApplicationProcessByProcessTypeRequest request);
}
