package gohigher.application.port.in;

import gohigher.application.Application;
import gohigher.common.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmptyScheduleApplicationResponse {

	private final long id;
	private final String companyName;
	private final String position;
	private final String specificPosition;
	private final String processType;
	private final String processDescription;

	public static EmptyScheduleApplicationResponse of(Application application, Process process) {
		return new EmptyScheduleApplicationResponse(
			application.getId(),
			application.getCompanyName(),
			application.getDuty(),
			application.getPosition(),
			process.getType().name(),
			process.getDescription()
		);
	}
}
