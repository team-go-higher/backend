package gohigher.application.port.in;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import gohigher.common.ProcessType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationProcessQueryService implements ApplicationProcessQueryPort {

	private final ApplicationProcessPersistenceQueryPort applicationProcessPersistenceQueryPort;

	@Override
	public List<ApplicationProcessByProcessTypeResponse> findByApplicationIdAndProcessType(Long applicationId,
		ProcessType processType) {
		return applicationProcessPersistenceQueryPort.findByApplicationIdAndProcessType(applicationId, processType)
			.stream()
			.map(ApplicationProcessByProcessTypeResponse::from)
			.toList();
	}
}
