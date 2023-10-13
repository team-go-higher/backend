package gohigher.application;

import java.util.List;

import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.entity.ApplicationRepository;
import gohigher.application.port.out.persistence.ApplicationPersistenceCommandPort;
import gohigher.common.Process;
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

	// @Override
	// public void updateCurrentProcessOrder(long id, long processId) {
	// 	ApplicationJpaEntity application = applicationRepository.findById(id)
	// 		.orElseThrow(() -> new GoHigherException(APPLICATION_NOT_FOUND));
	// 	ApplicationProcessJpaEntity process = applicationProcessRepository.findById(processId)
	// 		.orElseThrow(() -> new GoHigherException(APPLICATION_PROCESS_NOT_FOUND));
	//
	// 	application.updateCurrentProcess(process.getType(), process.getOrder());
	// }
}
