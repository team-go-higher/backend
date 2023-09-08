package gohigher.application.port.in;

public interface ApplicationCommandPort {

	void applySimply(Long userId, SimpleApplicationRequest command);

	long applySpecifically(Long userId, SpecificApplicationRequest command);
}
