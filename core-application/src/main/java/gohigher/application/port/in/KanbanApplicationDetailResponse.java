package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.application.CurrentProcess;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KanbanApplicationDetailResponse {

	private final Long id;
	private final String companyName;
	private final String duty;
	private final String detailedDuty;
	private final String processDescription;
	private final LocalDateTime schedule;

	public static KanbanApplicationDetailResponse from(CurrentProcess currentProcess) {
		return new KanbanApplicationDetailResponse(
			currentProcess.getId(),
			currentProcess.getCompanyName(),
			currentProcess.getDuty(),
			currentProcess.getDetailedDuty(),
			currentProcess.getDescription(),
			currentProcess.getSchedule()
		);
	}
}
