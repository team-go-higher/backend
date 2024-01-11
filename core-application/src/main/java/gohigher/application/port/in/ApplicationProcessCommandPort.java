package gohigher.application.port.in;

public interface ApplicationProcessCommandPort {

	ApplicationProcessByProcessTypeResponse register(Long userId, Long applicationId,
		UnscheduledProcessRequest request);
}
