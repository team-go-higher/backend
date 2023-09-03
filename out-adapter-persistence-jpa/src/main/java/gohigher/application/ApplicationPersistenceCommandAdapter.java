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
		ApplicationJpaEntity applicationJpaEntity =
			applicationRepository.save(ApplicationJpaEntity.from(userId, application));
		List<ApplicationProcessJpaEntity> applicationProcessJpaEntities = new ArrayList<>();
		List<Process> processes = application.getProcesses();
		for (int i = 0; i < processes.size(); i++) {
			applicationProcessJpaEntities.add(
				ApplicationProcessJpaEntity.from(applicationJpaEntity, processes.get(i), i));
		}
		applicationProcessRepository.saveAll(applicationProcessJpaEntities);

		return applicationJpaEntity.getId();
	}
}
