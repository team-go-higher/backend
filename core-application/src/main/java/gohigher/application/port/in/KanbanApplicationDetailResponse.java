package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.application.Application;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KanbanApplicationDetailResponse {

	private final Long applicationId;
	private final Long processId;
	private final String companyName;
	private final String position;
	private final String specificPosition;
	private final String processDescription;
	private final LocalDateTime schedule;

	public static KanbanApplicationDetailResponse from(Application application) {
		return new KanbanApplicationDetailResponse(
			application.getId(),
			application.getCurrentProcess().getId(),
			application.getCompanyName(),
			application.getPosition(),
			application.getSpecificPosition(),
			application.getCurrentProcess().getDescription(),
			application.getCurrentProcess().getSchedule()
		);
	}
}
