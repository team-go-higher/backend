package gohigher.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.port.in.ApplicationUseCase;
import gohigher.application.port.in.SimpleApplicationCommand;
import gohigher.application.port.out.persistence.ApplicationPersistencePort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService implements ApplicationUseCase {

	private final ApplicationPersistencePort applicationPersistencePort;

	@Override
	@Transactional
	public void applySimply(SimpleApplicationCommand command) {
		applicationPersistencePort.save(command.toDomain());
	}
}