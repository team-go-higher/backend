package gohigher.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

	@Override
	public List<Application> findByUserIdAndMonth(Long userId, int year, int month) {
		List<ApplicationJpaEntity> applicationJpaEntities =
			applicationRepository.findByUserIdAndMonth(userId, year, month);

		return convertToDomain(applicationJpaEntities);
	}

	@Override
	public List<Application> findByUserIdAndDate(long userId, LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
		List<ApplicationJpaEntity> applicationJpaEntities = applicationRepository.findByUserIdAndDate(userId,
			startOfDay, endOfDay);
		return convertToDomain(applicationJpaEntities);
	}

	private List<Application> convertToDomain(List<ApplicationJpaEntity> applicationJpaEntities) {
		return applicationJpaEntities.stream()
			.map(ApplicationJpaEntity::toCalenderDomain)
			.toList();
	}
}
