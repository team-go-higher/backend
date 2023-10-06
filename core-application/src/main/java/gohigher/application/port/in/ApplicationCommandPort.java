package gohigher.application.port.in;

public interface ApplicationCommandPort {

	SimpleApplicationRegisterResponse applySimply(Long userId, SimpleApplicationRequest request);

	long applySpecifically(Long userId, SpecificApplicationRequest request);

	void updateCurrentProcess(Long userId, CurrentProcessUpdateRequest request);
}
