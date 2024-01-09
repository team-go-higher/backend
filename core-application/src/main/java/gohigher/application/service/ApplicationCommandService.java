package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.Application;
import gohigher.application.ApplicationErrorCode;
import gohigher.application.port.in.ApplicationCommandPort;
import gohigher.application.port.in.CurrentProcessUpdateRequest;
import gohigher.application.port.in.SimpleApplicationRegisterResponse;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.application.port.in.SimpleApplicationUpdateRequest;
import gohigher.application.port.in.SpecificApplicationRequest;
import gohigher.application.port.out.persistence.ApplicationPersistenceCommandPort;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
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
	public SimpleApplicationRegisterResponse applySimply(Long userId, SimpleApplicationRequest request) {
		Application savedApplication = applicationPersistenceCommandPort.save(userId, request.toDomain());
		return SimpleApplicationRegisterResponse.from(savedApplication);
	}

	@Override
	public long applySpecifically(Long userId, SpecificApplicationRequest request) {
		return applicationPersistenceCommandPort.save(userId, request.toDomain())
			.getId();
	}

	@Override
	public void updateSimply(Long userId, Long applicationId, SimpleApplicationUpdateRequest request) {
		Application application = applicationPersistenceQueryPort.findByIdAndUserId(applicationId, userId)
			.orElseThrow(() -> new GoHigherException(ApplicationErrorCode.APPLICATION_NOT_FOUND));

		application.updateSimply(request.getCompanyName(), request.getPosition(), request.getUrl(),
			request.getProcessId(),
			request.getSchedule());

		applicationPersistenceCommandPort.updateSimply(request.getProcessId(), application);
	}

	@Override
	public void updateCurrentProcess(Long userId, CurrentProcessUpdateRequest request) {
		Long applicationId = request.getApplicationId();
		validateNotFound(userId, applicationId);
		validateProcessNotFound(request.getProcessId());
		applicationPersistenceCommandPort.updateCurrentProcessOrder(applicationId, userId, request.getProcessId());
	}

	@Override
	public void deleteApplication(Long userId, Long applicationId) {
		applicationPersistenceQueryPort.findByIdAndUserId(applicationId, userId)
			.orElseThrow(() -> new GoHigherException(ApplicationErrorCode.APPLICATION_NOT_FOUND));

		applicationPersistenceCommandPort.delete(applicationId);
	}

	private void validateNotFound(Long userId, Long applicationId) {
		if (!applicationPersistenceQueryPort.existsByIdAndUserId(applicationId, userId)) {
			throw new GoHigherException(APPLICATION_NOT_FOUND);
		}
	}

	private void validateProcessNotFound(Long processId) {
		if (!applicationProcessPersistenceQueryPort.existsById(processId)) {
			throw new GoHigherException(APPLICATION_PROCESS_NOT_FOUND);
		}
	}
}
