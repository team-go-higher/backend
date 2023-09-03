package gohigher.application.port.in;

public interface ApplicationCommandPort {

	void applySimply(Long userId, SimpleApplicationCommand command);
}