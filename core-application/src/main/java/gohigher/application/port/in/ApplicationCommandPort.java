package gohigher.application.port.in;

public interface ApplicationCommandPort {

	SimpleApplicationRegisterResponse applySimply(Long userId, SimpleApplicationRequest request);

	long applySpecifically(Long userId, SpecificApplicationRequest request);

	void updateSimply(Long userId, Long applicationId, SimpleApplicationUpdateRequest request);

	void updateSpecifically(Long userId, Long applicationId, SpecificApplicationRequest request);

	void updateCurrentProcess(Long userId, CurrentProcessUpdateRequest request);

	void deleteApplication(Long userId, Long applicationId);

	CompletedUpdatingResponse updateCompleted(Long userId, Long applicationId, CompletedUpdatingRequest request);
}
