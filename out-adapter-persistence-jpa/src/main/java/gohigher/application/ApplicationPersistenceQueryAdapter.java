package gohigher.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationRepository;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.common.ProcessType;
import gohigher.pagination.PagingContainer;
import gohigher.pagination.domain.PageRequestGenerator;
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
	public PagingContainer<Application> findUnscheduledByUserId(Long userId, int page, int size) {
		Slice<ApplicationJpaEntity> applicationJpaEntities = applicationRepository.findUnscheduledByUserId(userId,
			PageRequestGenerator.of(page, size));

		List<Application> applications = applicationJpaEntities.stream()
			.map(ApplicationJpaEntity::toCalenderDomain)
			.toList();
		return new PagingContainer<>(applicationJpaEntities.hasNext(), applications);
	}

	@Override
	public PagingContainer<Application> findOnlyCurrentProcessByUserIdAndProcessType(Long userId, ProcessType processType, int page, int size) {
		Slice<ApplicationJpaEntity> applicationJpaEntities =
			applicationRepository.findOnlyCurrentProcessByUserIdAndProcessType(userId, processType,
				PageRequestGenerator.of(page, size));

		List<Application> applications = applicationJpaEntities.stream()
			.map(ApplicationJpaEntity::toKanbanDomain)
			.toList();
		return new PagingContainer<>(applicationJpaEntities.hasNext(), applications);
	}

	private List<Application> convertToDomain(List<ApplicationJpaEntity> applicationJpaEntities) {
		return applicationJpaEntities.stream()
			.map(ApplicationJpaEntity::toCalenderDomain)
			.toList();
	}
}
