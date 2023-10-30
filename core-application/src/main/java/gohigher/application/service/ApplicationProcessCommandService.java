package gohigher.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.ApplicationErrorCode;
import gohigher.application.port.in.ApplicationProcessByProcessTypeResponse;
import gohigher.application.port.in.ApplicationProcessCommandPort;
import gohigher.application.port.in.SimpleApplicationProcessRequest;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceCommandPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import gohigher.global.exception.GoHigherException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationProcessCommandService implements ApplicationProcessCommandPort {

	private final ApplicationPersistenceQueryPort applicationPersistenceQueryPort;
	private final ApplicationProcessPersistenceCommandPort applicationProcessPersistenceCommandPort;
	private final ApplicationProcessPersistenceQueryPort applicationProcessPersistenceQueryPort;

	@Override
	public ApplicationProcessByProcessTypeResponse register(Long userId, long applicationId,
		SimpleApplicationProcessRequest request) {
		validateAuthorizationOfUser(userId, applicationId);

		ProcessType type = ProcessType.valueOf(request.getType());
		Process process = makeNewProcess(applicationId, type, request.getDescription());
		Process savedProcess = applicationProcessPersistenceCommandPort.save(applicationId, process);
		return ApplicationProcessByProcessTypeResponse.from(savedProcess);
	}

	private void validateAuthorizationOfUser(Long userId, Long applicationId) {
		if (!applicationPersistenceQueryPort.existsByIdAndUserId(applicationId, userId)) {
			throw new GoHigherException(ApplicationErrorCode.APPLICATION_NOT_FOUND);
		}
	}

	private Process makeNewProcess(long applicationId, ProcessType type, String description) {
		List<Process> applicationProcessesByType = applicationProcessPersistenceQueryPort.findByApplicationIdAndProcessType(
			applicationId, type);

		if (applicationProcessesByType.isEmpty()) {
			return Process.makeFirstByType(type, description);
		}

		Process lastProcess = applicationProcessesByType.get(applicationProcessesByType.size() - 1);
		return new Process(type, description, null, lastProcess.getOrder() + 1);
	}
}

