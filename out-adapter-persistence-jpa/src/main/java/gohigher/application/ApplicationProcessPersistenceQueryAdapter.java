package gohigher.application;

import java.util.Optional;

import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import gohigher.common.Process;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationProcessPersistenceQueryAdapter implements ApplicationProcessPersistenceQueryPort {

	private final ApplicationProcessRepository applicationProcessRepository;

	@Override
	public Optional<Process> findById(Long id) {
		return applicationProcessRepository.findById(id)
			.map(ApplicationProcessJpaEntity::toDomain);
	}
}
