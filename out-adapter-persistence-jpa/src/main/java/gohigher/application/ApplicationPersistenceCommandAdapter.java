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
	public void updateSimply(Long processId, Application application) {
		update(application);
		ApplicationProcessJpaEntity applicationProcessJpaEntity = applicationProcessRepository.findById(processId)
			.orElseThrow(() -> new GoHigherException(APPLICATION_PROCESS_NOT_FOUND));

		applicationProcessJpaEntity.update(application.getProcessById(processId));
	}

	@Override
	public void updateSpecifically(Application application) {
		ApplicationJpaEntity applicationJpaEntity = update(application);
		updateApplicationProcess(applicationJpaEntity, application.getProcesses(), application.getCurrentProcess());
	}

	@Override
	public void updateCompleted(Application application) {
		update(application);
	}

	@Override
	public void delete(long id) {
		ApplicationJpaEntity applicationJpaEntity = applicationRepository.findById(id)
			.orElseThrow(() -> new GoHigherException(APPLICATION_NOT_FOUND));

		applicationJpaEntity.delete();
	}

	private ApplicationJpaEntity update(Application application) {
		ApplicationJpaEntity applicationJpaEntity = applicationRepository.findById(application.getId())
			.orElseThrow(() -> new GoHigherException(APPLICATION_NOT_FOUND));

		applicationJpaEntity.update(application);
		return applicationJpaEntity;
	}

	private void saveApplicationProcesses(ApplicationJpaEntity applicationJpaEntity,
		List<Process> processes, Process currentProcess) {
		for (Process process : processes) {
			applicationJpaEntity.addProcess(ApplicationProcessJpaEntity.of(
				applicationJpaEntity, process, process == currentProcess));
		}

		applicationProcessRepository.saveAll(applicationJpaEntity.getProcesses());
	}

	private void updateApplicationProcess(ApplicationJpaEntity application, List<Process> processes,
		Process currentProcess) {
		resetProcesses(application);
		saveApplicationProcesses(application, processes, currentProcess);
	}

	private void resetProcesses(ApplicationJpaEntity application) {
		applicationProcessRepository.deleteByApplicationId(application.getId());
		application.resetProcesses();
	}
}
