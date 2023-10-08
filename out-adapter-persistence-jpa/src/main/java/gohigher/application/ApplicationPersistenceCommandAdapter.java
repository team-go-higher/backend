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
	public Long save(Long userId, Application application) {
		ApplicationJpaEntity applicationJpaEntity = applicationRepository.save(
			ApplicationJpaEntity.of(application, userId));

		List<Process> processes = application.getProcesses();
		saveApplicationProcesses(applicationJpaEntity, processes);
		return applicationJpaEntity.getId();
	}

	private void saveApplicationProcesses(ApplicationJpaEntity applicationJpaEntity, List<Process> processes) {
		List<ApplicationProcessJpaEntity> applicationProcessJpaEntities = processes.stream()
			.map(process -> ApplicationProcessJpaEntity.of(applicationJpaEntity, process))
			.toList();

		applicationProcessRepository.saveAll(applicationProcessJpaEntities);
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
