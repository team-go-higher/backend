package gohigher.application;

import java.util.ArrayList;
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
	public Long save(Long userId, Application application) {
		ApplicationJpaEntity applicationJpaEntity = applicationRepository.save(ApplicationJpaEntity.of(application, userId));

		List<Process> processes = application.getProcesses();
		saveApplicationProcesses(applicationJpaEntity, processes);
		return applicationJpaEntity.getId();
	}

	private void saveApplicationProcesses(ApplicationJpaEntity applicationJpaEntity, List<Process> processes) {
		List<ApplicationProcessJpaEntity> applicationProcessJpaEntities = new ArrayList<>();
		for (int i = 0; i < processes.size(); i++) {
			applicationProcessJpaEntities.add(ApplicationProcessJpaEntity.of(applicationJpaEntity, processes.get(i), i));
		}
		applicationProcessRepository.saveAll(applicationProcessJpaEntities);
	}

	@Override
	public void updateCurrentProcessOrder(long id, long processId) {
		applicationRepository.updateCurrentProcessOrder(id, processId);
	}
}
