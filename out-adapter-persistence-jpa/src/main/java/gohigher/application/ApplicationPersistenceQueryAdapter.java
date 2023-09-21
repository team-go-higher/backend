package gohigher.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationRepository;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.pagination.port.in.PagingParameters;
import gohigher.pagination.port.in.SliceContainer;
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
	public Optional<Application> findByIdAndUserId(Long id, Long userId) {
		return applicationRepository.findByIdAndUserIdWithProcess(id, userId)
			.map(ApplicationJpaEntity::toDomain);
	}

	@Override
	public List<Application> findByUserIdAndMonth(Long userId, int year, int month) {
		List<ApplicationJpaEntity> applicationJpaEntities =
			applicationRepository.findByUserIdAndMonth(userId, year, month);

		return convertToDomain(applicationJpaEntities);
	}

	@Override
	public List<Application> findByUserIdAndDate(Long userId, LocalDate date) {
		LocalDateTime startOfDate = date.atStartOfDay();
		LocalDateTime endOfDate = date.plusDays(1).atStartOfDay();
		List<ApplicationJpaEntity> applicationJpaEntities = applicationRepository.findByUserIdAndDate(userId,
			startOfDate, endOfDate);
		return convertToDomain(applicationJpaEntities);
	}

	@Override
	public SliceContainer<Application> findByUserIdWithoutSchedule(Long userId, PagingParameters pagingParameters) {
		return new SliceContainer<>(
			applicationRepository.findByUserIdWithoutSchedule(userId, pagingParameters.toPageable())
			.map(ApplicationJpaEntity::toDomain)
		);
	}

	@Override
	public List<Application> findOnlyWithCurrentProcessByUserId(Long userId) {
		List<ApplicationJpaEntity> applications = applicationRepository.findOnlyWithCurrentProcessByUserId(userId);
		return convertToKanbanApplication(applications);
	}

	private List<Application> convertToDomain(List<ApplicationJpaEntity> applicationJpaEntities) {
		return applicationJpaEntities.stream()
			.map(ApplicationJpaEntity::toCalenderDomain)
			.toList();
	}

	private List<Application> convertToKanbanApplication(List<ApplicationJpaEntity> applications) {
		return applications.stream()
			.map(ApplicationJpaEntity::toKanbanDomain)
			.toList();
	}
}
