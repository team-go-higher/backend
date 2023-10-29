package gohigher.application.port.in;

import gohigher.application.Application;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KanbanByProcessApplicationResponse {

	private final long applicationId;
	private final String companyName;
	private final String position;
	private final String specificPosition;
	private final ProcessResponse process;

	public static KanbanByProcessApplicationResponse from(Application application) {
		return new KanbanByProcessApplicationResponse(
			application.getId(),
			application.getCompanyName(),
			application.getPosition(),
			application.getSpecificPosition(),
			ProcessResponse.from(application.getCurrentProcess())
		);
	}
}
