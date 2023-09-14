package gohigher.application.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.Application;
import gohigher.application.port.in.ApplicationQueryPort;
import gohigher.application.port.in.CalendarApplicationRequest;
import gohigher.application.port.in.CalendarApplicationResponse;
import gohigher.application.port.in.DateApplicationRequest;
import gohigher.application.port.in.DateApplicationResponse;
import gohigher.application.port.in.ProcessResponse;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicationQueryService implements ApplicationQueryPort {

	private final ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Override
	public List<CalendarApplicationResponse> findByMonth(CalendarApplicationRequest request) {
		List<Application> applications = applicationPersistenceQueryPort.findByUserIdAndMonth(request.getUserId(),
			request.getYear(), request.getMonth());

		return applications.stream()
			.flatMap(this::extractCalendarResponses)
			.toList();
	}

	@Override
	public List<DateApplicationResponse> findByDate(DateApplicationRequest request) {
		List<Application> applications = applicationPersistenceQueryPort.findByUserIdAndDate(request.getUserId(),
			request.getDate());
		return applications.stream()
			.flatMap(this::extractDateApplicationResponses)
			.toList();
	}

	private Stream<CalendarApplicationResponse> extractCalendarResponses(Application application) {
		return application.getProcesses().stream()
			.map(process -> new CalendarApplicationResponse(application.getId(), process.getId(),
				application.getCompanyName(), process.getType().name(), process.getSchedule()));
	}

	private Stream<DateApplicationResponse> extractDateApplicationResponses(Application application) {
		return application.getProcesses().stream()
			.map(ProcessResponse::from)
			.map(processResponse -> new DateApplicationResponse(application.getId(), application.getCompanyName(),
				processResponse));
	}
}
