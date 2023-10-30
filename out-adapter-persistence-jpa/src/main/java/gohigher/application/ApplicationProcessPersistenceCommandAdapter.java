package gohigher.application;

import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.entity.ApplicationRepository;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceCommandPort;
import gohigher.common.Process;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationProcessPersistenceCommandAdapter implements ApplicationProcessPersistenceCommandPort {

	private final ApplicationRepository applicationRepository;
	private final ApplicationProcessRepository applicationProcessRepository;

	@Override
	public Process save(long applicationId, Process process) {
		ApplicationJpaEntity application = applicationRepository.getReferenceById(applicationId);
		ApplicationProcessJpaEntity applicationProcess = ApplicationProcessJpaEntity.of(application, process, false);
		return applicationProcessRepository.save(applicationProcess)
			.toDomain();
	}
}
