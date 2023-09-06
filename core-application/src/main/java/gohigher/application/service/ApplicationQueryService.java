package gohigher.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import gohigher.application.Application;
import gohigher.application.port.in.ApplicationCalendarResponse;
import gohigher.application.port.in.ApplicationMonthQueryResponse;
import gohigher.application.port.in.ApplicationQueryPort;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.common.Process;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationQueryService implements ApplicationQueryPort {

	private final ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Override
	public ApplicationMonthQueryResponse findByMonth(Long userId, int year, int month) {
		List<Application> applications = applicationPersistenceQueryPort.findByIdAndMonth(userId, year, month);

		List<ApplicationCalendarResponse> applicationCalendarResponses = new ArrayList<>();
		for (Application application : applications) {
			addScheduleResponses(application, applicationCalendarResponses);
		}
		return new ApplicationMonthQueryResponse(applicationCalendarResponses);
	}

	private void addScheduleResponses(Application application,
		List<ApplicationCalendarResponse> applicationCalendarResponses) {
		for (Process process : application.getProcesses()) {
			applicationCalendarResponses.add(
				new ApplicationCalendarResponse(application.getCompanyName(), process.getType().name(),
					process.getSchedule()));
		}
	}
}
