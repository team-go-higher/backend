package gohigher.application;

import java.util.List;
import java.util.Optional;

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
	public boolean existsByIdAndUserId(Long id, Long userId) {
		return applicationRepository.existsByIdAndUserId(id, userId);
	}

	@Override
	public List<Application> findByIdAndMonth(Long userId, int year, int month) {
		List<ApplicationJpaEntity> applicationJpaEntities =
			applicationRepository.findByUserIdAndDate(userId, year, month);

		return applicationJpaEntities.stream()
			.map(ApplicationJpaEntity::toDomain)
			.toList();
	}

	@Override
	public Optional<Application> findByIdAndUserId(Long id, Long userId) {
		Optional<ApplicationJpaEntity> applicationJpaEntity = applicationRepository.findByIdAndUserId(id, userId);

		return applicationJpaEntity.map(ApplicationJpaEntity::toDomain);
	}
}
