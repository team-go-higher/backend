package gohigher.application.port.in;

import gohigher.application.Application;
import gohigher.common.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UnscheduledApplicationResponse {

	private final long applicationId;
	private final String companyName;
	private final String position;
	private final String specificPosition;
	private final ProcessResponse process;

	public static UnscheduledApplicationResponse of(Application application, Process process) {
		return new UnscheduledApplicationResponse(
			application.getId(),
			application.getCompanyName(),
			application.getPosition(),
			application.getSpecificPosition(),
			ProcessResponse.from(process)
		);
	}
}
