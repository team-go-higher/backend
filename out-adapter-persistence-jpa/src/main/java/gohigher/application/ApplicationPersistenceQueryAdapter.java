package gohigher.application;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationRepository;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationPersistenceQueryAdapter implements ApplicationPersistenceQueryPort {

	private final ApplicationRepository applicationRepository;

	@Override
	public Optional<Application> findById(Long id) {
		return applicationRepository.findById(id)
			.map(ApplicationJpaEntity::toDomain);
	}

	public List<Application> findByIdAndMonth(Long userId, int year, int month) {
		List<ApplicationJpaEntity> applicationJpaEntities =
			applicationRepository.findByUserIdAndDate(userId, year, month);

		return applicationJpaEntities.stream()
			.map(ApplicationJpaEntity::toDomain)
			.toList();
	}
}
