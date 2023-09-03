package gohigher.application.port.in;

public interface ApplicationUseCase {

	void applySimply(Long userId, SimpleApplicationCommand command);
}