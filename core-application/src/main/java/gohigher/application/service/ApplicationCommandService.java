package gohigher.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.port.in.ApplicationCommandPort;
import gohigher.application.port.in.SimpleApplicationCommand;
import gohigher.application.port.out.persistence.ApplicationPersistenceCommandPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationCommandService implements ApplicationCommandPort {

	private final ApplicationPersistenceCommandPort applicationPersistenceCommandPort;

	@Override
	@Transactional
	public void applySimply(Long userId, SimpleApplicationCommand command) {
		applicationPersistenceCommandPort.save(userId, command.toDomain());
	}
}