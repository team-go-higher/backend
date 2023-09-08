package gohigher.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.port.in.ApplicationCommandPort;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.application.port.out.persistence.ApplicationPersistenceCommandPort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationCommandService implements ApplicationCommandPort {

	private final ApplicationPersistenceCommandPort applicationPersistenceCommandPort;

	@Override
	public void applySimply(Long userId, SimpleApplicationRequest command) {
		applicationPersistenceCommandPort.save(userId, command.toDomain());
	}
}