package gohigher.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.Application;
import gohigher.application.port.in.ApplicationQueryPort;
import gohigher.application.port.in.CalenderApplicationRequest;
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
	public List<CalenderApplicationResponse> findByMonth(CalenderApplicationRequest request) {
		List<Application> applications = applicationPersistenceQueryPort.findByIdAndMonth(request.getUserId(),
			request.getYear(), request.getMonth());

		List<CalenderApplicationResponse> calenderApplicationResponses = new ArrayList<>();
		for (Application application : applications) {
			addScheduleResponses(application, calenderApplicationResponses);
		}
		return calenderApplicationResponses;
	}

	private void addScheduleResponses(Application application,
		List<CalenderApplicationResponse> calenderApplicationRespons) {
		for (Process process : application.getProcesses()) {
			calenderApplicationRespons.add(
				new CalenderApplicationResponse(application.getId(), application.getCompanyName(),
					process.getType().name(), process.getSchedule()));
		}
	}
}
