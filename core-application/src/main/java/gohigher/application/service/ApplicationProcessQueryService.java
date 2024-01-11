package gohigher.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.ApplicationErrorCode;
import gohigher.application.port.in.ApplicationProcessByProcessTypeResponse;
import gohigher.application.port.in.ApplicationProcessQueryPort;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import gohigher.common.ProcessType;
import gohigher.global.exception.GoHigherException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationProcessQueryService implements ApplicationProcessQueryPort {

	private final ApplicationPersistenceQueryPort applicationPersistenceQueryPort;
	private final ApplicationProcessPersistenceQueryPort applicationProcessPersistenceQueryPort;

	@Override
	public List<ApplicationProcessByProcessTypeResponse> findByApplicationIdAndProcessType(Long userId,
		Long applicationId, ProcessType processType) {
		validateAuthorizationOfUser(userId, applicationId);
		return applicationProcessPersistenceQueryPort.findByApplicationIdAndProcessType(applicationId, processType)
			.stream()
			.map(ApplicationProcessByProcessTypeResponse::from)
			.toList();
	}

	private void validateAuthorizationOfUser(Long userId, Long applicationId) {
		if (!applicationPersistenceQueryPort.existsByIdAndUserId(applicationId, userId)) {
			throw new GoHigherException(ApplicationErrorCode.APPLICATION_NOT_FOUND);
		}
	}
}
