package gohigher.application;

import java.util.List;

import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationProcessPersistenceQueryAdapter implements ApplicationProcessPersistenceQueryPort {

	private final ApplicationProcessRepository applicationProcessRepository;

	@Override
	public boolean existsById(Long id) {
		return applicationProcessRepository.existsById(id);
	}

	@Override
	public List<Process> findByApplicationIdAndProcessType(Long applicationId, ProcessType processType) {
		return applicationProcessRepository.findByApplicationIdAndType(applicationId, processType)
			.stream()
			.map(ApplicationProcessJpaEntity::toDomain)
			.toList();
	}
}
