package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.application.Application;
import gohigher.common.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CalendarApplicationResponse {

	private final Long applicationId;
	private final Long processId;
	private final String name;
	private final String processType;
	private final LocalDateTime schedule;

	public static CalendarApplicationResponse of(Application application, Process process) {
		return new CalendarApplicationResponse(application.getId(), process.getId(), application.getCompanyName(),
			process.getType().name(), process.getSchedule());
	}
}
