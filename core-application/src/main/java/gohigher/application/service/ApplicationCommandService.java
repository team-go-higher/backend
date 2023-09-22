package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.Application;
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
		Application application = request.toDomain();
		assignAllProcessOrder(application);
		applicationPersistenceCommandPort.save(userId, application);
	}

	@Override
	public long applySpecifically(Long userId, SpecificApplicationRequest request) {
		Application application = request.toDomain();
		assignAllProcessOrder(application);
		return applicationPersistenceCommandPort.save(userId, application);
	}

	private void assignAllProcessOrder(Application application) {
		int nextOrder = 1;
		for (Process process : application.getProcesses()) {
			process.assignOrder(nextOrder);
			nextOrder++;
		}
	}

	@Override
	public void updateCurrentProcess(Long userId, CurrentProcessUpdateRequest request) {
		Long applicationId = request.getApplicationId();
		validateNotFound(userId, applicationId);
		validateProcessNotFound(request.getProcessId());
		applicationPersistenceCommandPort.updateCurrentProcessOrder(applicationId, request.getProcessId());
	}

	private void validateNotFound(Long userId, Long applicationId) {
		if (!applicationPersistenceQueryPort.existsByIdAndUserId(userId, applicationId)) {
			throw new GoHigherException(APPLICATION_NOT_FOUND);
		}
	}

	private void validateProcessNotFound(Long processId) {
		if (!applicationProcessPersistenceQueryPort.existsById(processId)) {
			throw new GoHigherException(APPLICATION_PROCESS_NOT_FOUND);
		}
	}
}
