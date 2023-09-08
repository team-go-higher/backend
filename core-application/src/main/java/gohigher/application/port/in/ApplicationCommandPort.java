package gohigher.application.port.in;

public interface ApplicationCommandPort {

	void applySimply(Long userId, SimpleApplicationRequest command);

	void applySpecifically(Long userId, SpecificApplicationRequest command);
}
