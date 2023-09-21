package gohigher.application.port.in;

import gohigher.application.Application;
import gohigher.common.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmptyScheduleApplicationResponse {

	private final long applicationId;
	private final long processId;
	private final String companyName;
	private final String position;
	private final String specificPosition;
	private final String processType;
	private final String processDescription;

	public static EmptyScheduleApplicationResponse of(Application application, Process process) {
		return new EmptyScheduleApplicationResponse(
			application.getId(),
			process.getId(),
			application.getCompanyName(),
			application.getPosition(),
			application.getSpecificPosition(),
			process.getType().name(),
			process.getDescription()
		);
	}
}
