package gohigher.application.port.in;

public interface ApplicationCommandPort {

	void applySimply(Long userId, SimpleApplicationRequest request);

	long applySpecifically(Long userId, SpecificApplicationRequest request);

	void updateCurrentProcess(Long userId, CurrentProcessUpdateRequest request);

	void updateSpecifically(Long userId, long applicationId, SpecificApplicationUpdateRequest request);
}
