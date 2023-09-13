package gohigher.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import gohigher.application.dto.CurrentProcessDto;
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

	private List<Application> convertToDomain(List<ApplicationJpaEntity> applicationJpaEntities) {
		return applicationJpaEntities.stream()
			.map(ApplicationJpaEntity::toCalenderDomain)
			.toList();
	}

	@Override
	public List<CurrentProcess> findCurrentProcessByUserId(Long userId) {
		List<CurrentProcessDto> currentProcesses = applicationRepository.findCurrentProcessByUserId(userId);
		return currentProcesses.stream()
			.map(CurrentProcessDto::toDomain)
			.toList();
	}
}
