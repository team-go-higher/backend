package gohigher.application;

import static gohigher.application.ApplicationErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.entity.ApplicationRepository;
import gohigher.application.port.out.persistence.ApplicationPersistenceCommandPort;
import gohigher.common.Process;
import gohigher.global.exception.GoHigherException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationPersistenceCommandAdapter implements ApplicationPersistenceCommandPort {

	private final ApplicationRepository applicationRepository;
	private final ApplicationProcessRepository applicationProcessRepository;

	@Override
	public Application save(Long userId, Application application) {
		ApplicationJpaEntity savedApplicationJpaEntity = applicationRepository.save(
			ApplicationJpaEntity.of(application, userId));
		List<ApplicationProcessJpaEntity> savedApplicationProcessJpaEntities =
			saveApplicationProcesses(savedApplicationJpaEntity, application.getProcesses());

		return savedApplicationJpaEntity.toDomain(savedApplicationProcessJpaEntities);
	}

	private List<ApplicationProcessJpaEntity> saveApplicationProcesses(ApplicationJpaEntity applicationJpaEntity,
		List<Process> processes) {
		List<ApplicationProcessJpaEntity> applicationProcessJpaEntities = processes.stream()
			.map(process -> ApplicationProcessJpaEntity.of(applicationJpaEntity, process))
			.toList();
		return applicationProcessRepository.saveAll(applicationProcessJpaEntities);
	}

	@Override
	public void updateCurrentProcessOrder(long id, long processId) {
		ApplicationJpaEntity application = applicationRepository.findById(id)
			.orElseThrow(() -> new GoHigherException(APPLICATION_NOT_FOUND));
		ApplicationProcessJpaEntity process = applicationProcessRepository.findById(processId)
			.orElseThrow(() -> new GoHigherException(APPLICATION_PROCESS_NOT_FOUND));

		application.updateCurrentProcess(process.getType(), process.getOrder());
	}
}
