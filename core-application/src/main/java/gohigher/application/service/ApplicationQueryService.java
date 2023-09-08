package gohigher.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.Application;
import gohigher.application.port.in.ApplicationQueryPort;
import gohigher.application.port.in.CalenderApplicationMonthResponse;
import gohigher.application.port.in.CalenderApplicationResponse;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.common.Process;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicationQueryService implements ApplicationQueryPort {

	private final ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Override
	public CalenderApplicationMonthResponse findByMonth(Long userId, int year, int month) {
		List<Application> applications = applicationPersistenceQueryPort.findByIdAndMonth(userId, year, month);

		List<CalenderApplicationResponse> calenderApplicationRespons = new ArrayList<>();
		for (Application application : applications) {
			addScheduleResponses(application, calenderApplicationRespons);
		}
		return new CalenderApplicationMonthResponse(calenderApplicationRespons);
	}

	private void addScheduleResponses(Application application,
		List<CalenderApplicationResponse> calenderApplicationRespons) {
		for (Process process : application.getProcesses()) {
			calenderApplicationRespons.add(
				new CalenderApplicationResponse(application.getCompanyName(), process.getType().name(),
					process.getSchedule()));
		}
	}
}
