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
import gohigher.common.ProcessType;
import gohigher.global.exception.GoHigherException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationCommandService implements ApplicationCommandPort {

	private final ApplicationPersistenceCommandPort applicationPersistenceCommandPort;
	private final ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Override
	public void applySimply(Long userId, SimpleApplicationRequest request) {
		applicationPersistenceCommandPort.save(request.toDomain(userId));
	}

	@Override
	public long applySpecifically(Long userId, SpecificApplicationRequest request) {
		return applicationPersistenceCommandPort.save(request.toDomain(userId));
	}

	@Override
	public void updateCurrentProcess(Long userId, CurrentProcessUpdateRequest request) {
		Long applicationId = request.getApplicationId();
		Application application = applicationPersistenceQueryPort.findById(applicationId)
			.orElseThrow(() -> new GoHigherException(APPLICATION_NOT_FOUND));
		validateForbidden(application, userId);
		ProcessType processType = ProcessType.from(request.getCurrentProcessType());
		int processOrder = application.getProcessOrderOfType(processType);
		applicationPersistenceCommandPort.updateCurrentProcessOrder(applicationId, processOrder);
	}

	private void validateForbidden(Application application, Long userId) {
		if (!application.isAppliedBy(userId)) {
			throw new GoHigherException(APPLICATION_FORBIDDEN);
		}
	}
}
