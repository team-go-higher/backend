package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.application.Application;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KanbanByProcessApplicationResponse {

	private final long applicationId;
	private final long processId;
	private final String companyName;
	private final String position;
	private final String specificPosition;
	private final String processDescription;
	private final LocalDateTime schedule;

	public static KanbanByProcessApplicationResponse from(Application application) {
		return new KanbanByProcessApplicationResponse(
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
