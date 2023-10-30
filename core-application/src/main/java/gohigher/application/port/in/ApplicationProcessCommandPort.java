package gohigher.application.port.in;

public interface ApplicationProcessCommandPort {

	ApplicationProcessByProcessTypeResponse register(Long userId, long applicationId,
		UnscheduledProcessRequest request);
}
