package gohigher.application;

import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationProcessPersistenceQueryAdapter implements ApplicationProcessPersistenceQueryPort {

	private final ApplicationProcessRepository applicationProcessRepository;

	@Override
	public boolean existsById(Long id) {
		return applicationProcessRepository.existsById(id);
	}
}
