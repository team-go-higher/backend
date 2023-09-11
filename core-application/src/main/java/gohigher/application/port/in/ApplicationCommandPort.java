package gohigher.application.port.in;

public interface ApplicationCommandPort {

	void applySimply(Long userId, SimpleApplicationRequest request);

	long applySpecifically(Long userId, SpecificApplicationRequest request);
}
