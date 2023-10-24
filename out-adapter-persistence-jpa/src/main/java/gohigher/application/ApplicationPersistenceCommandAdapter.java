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
		saveApplicationProcesses(savedApplicationJpaEntity, application.getProcesses(),
			application.getCurrentProcess());

		return savedApplicationJpaEntity.toDomain();
	}

	private void saveApplicationProcesses(ApplicationJpaEntity applicationJpaEntity,
		List<Process> processes, Process currentProcess) {
		for (Process process : processes) {
			applicationJpaEntity.addProcess(ApplicationProcessJpaEntity.of(
				applicationJpaEntity, process, process == currentProcess));
		}

		applicationProcessRepository.saveAll(applicationJpaEntity.getProcesses());
	}

	@Override
	public void updateCurrentProcessOrder(long id, long userId, long processId) {
		ApplicationJpaEntity application = applicationRepository.findByIdAndUserIdWithProcess(id, userId)
			.orElseThrow(() -> new GoHigherException(APPLICATION_NOT_FOUND));

		for (ApplicationProcessJpaEntity process : application.getProcesses()) {
			if (process.isCurrent()) {
				process.changeToNonCurrentProcess();
			}

			if (process.getId() == processId) {
				process.changeToCurrentProcess();
			}
		}
	}

	@Override
	public void updateSimply(Long applicationId, Long processId, Application application) {
		ApplicationJpaEntity applicationJpaEntity = applicationRepository.findById(applicationId)
			.orElseThrow(() -> new GoHigherException(APPLICATION_NOT_FOUND));

		applicationJpaEntity.update(application);

		ApplicationProcessJpaEntity applicationProcessJpaEntity = applicationProcessRepository.findById(processId)
			.orElseThrow(() -> new GoHigherException(APPLICATION_PROCESS_NOT_FOUND));

		applicationProcessJpaEntity.update(application.getProcessById(processId));
	}
}
