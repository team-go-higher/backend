package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.port.in.ApplicationCommandPort;
import gohigher.application.port.in.CurrentProcessUpdateRequest;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.application.port.in.SpecificApplicationRequest;
import gohigher.application.port.out.persistence.ApplicationPersistenceCommandPort;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import gohigher.common.Process;
import gohigher.global.exception.GoHigherException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationCommandService implements ApplicationCommandPort {

	private final ApplicationPersistenceCommandPort applicationPersistenceCommandPort;
	private final ApplicationProcessPersistenceQueryPort applicationProcessPersistenceQueryPort;
	private final ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Override
	public void applySimply(Long userId, SimpleApplicationRequest request) {
		applicationPersistenceCommandPort.save(userId, request.toDomain());
	}

	@Override
	public long applySpecifically(Long userId, SpecificApplicationRequest request) {
		return applicationPersistenceCommandPort.save(userId, request.toDomain());
	}

	@Override
	public void updateCurrentProcess(Long userId, CurrentProcessUpdateRequest request) {
		Long applicationId = request.getApplicationId();
		validateNotFound(userId, applicationId);
		Process process = applicationProcessPersistenceQueryPort.findById(request.getProcessId())
			.orElseThrow(() -> new GoHigherException(APPLICATION_PROCESS_NOT_FOUND));
		applicationPersistenceCommandPort.updateCurrentProcessOrder(applicationId, process.getId());
	}

	private void validateNotFound(Long userId, Long applicationId) {
		if (!applicationPersistenceQueryPort.existsByIdAndUserId(userId, applicationId)) {
			throw new GoHigherException(APPLICATION_NOT_FOUND);
		}
	}
}
