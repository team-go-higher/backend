package gohigher.application.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.Application;
import gohigher.application.port.in.ApplicationQueryPort;
import gohigher.application.port.in.CalenderApplicationRequest;
import gohigher.application.port.in.CalenderApplicationResponse;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicationQueryService implements ApplicationQueryPort {

	private final ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Override
	public List<CalenderApplicationResponse> findByMonth(CalenderApplicationRequest request) {
		List<Application> applications = applicationPersistenceQueryPort.findByIdAndMonth(request.getUserId(),
			request.getYear(), request.getMonth());

		return applications.stream()
			.flatMap(ApplicationQueryService::extractCalenderResponses)
			.toList();
	}

	private static Stream<CalenderApplicationResponse> extractCalenderResponses(Application application) {
		return application.getProcesses().stream()
			.map(process -> new CalenderApplicationResponse(application.getId(), process.getId(),
				application.getCompanyName(), process.getType().name(), process.getSchedule()));
	}
}
