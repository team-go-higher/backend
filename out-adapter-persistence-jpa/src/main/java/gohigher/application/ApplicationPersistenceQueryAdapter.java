package gohigher.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationRepository;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.search.ApplicationSortingType;
import gohigher.common.ProcessType;
import gohigher.pagination.PagingContainer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationPersistenceQueryAdapter implements ApplicationPersistenceQueryPort {

	private static final int DIFFERENCES_PAGES_AND_DB_INDEX = 1;

	private final ApplicationRepository applicationRepository;

	@Override
	public PagingContainer<Application> findAllByUserId(Long userId, int page, int size,
		ApplicationSortingType sortingType,
		List<ProcessType> process, List<Boolean> completed, String companyName, LocalDateTime today) {
		Slice<ApplicationJpaEntity> applicationJpaEntities = applicationRepository.findAllByUserId(userId,
			PageRequest.of(page - DIFFERENCES_PAGES_AND_DB_INDEX, size),
			sortingType, process, completed, companyName, today);

		return convertToPagingContainer(applicationJpaEntities);
	}

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
			PageRequest.of(page - DIFFERENCES_PAGES_AND_DB_INDEX, size));
		return convertToPagingContainer(applicationJpaEntities);
	}

	@Override
	public List<Application> findOnlyWithCurrentProcessByUserId(Long userId) {
		List<ApplicationJpaEntity> applications = applicationRepository.findOnlyWithCurrentProcessByUserId(userId);
		return convertToKanbanApplication(applications);
	}

	@Override
	public List<Application> findOnlyCurrentProcessByUserIdAndProcessType(Long userId, ProcessType processType) {
		List<ApplicationJpaEntity> applications = applicationRepository.findOnlyCurrentProcessByUserIdAndProcessType(
			userId, processType);
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

	private PagingContainer<Application> convertToPagingContainer(Slice<ApplicationJpaEntity> applicationJpaEntities) {
		List<Application> applications = applicationJpaEntities.stream()
			.map(ApplicationJpaEntity::toCalenderDomain)
			.toList();
		return new PagingContainer<>(applicationJpaEntities.hasNext(), applications);
	}
}
