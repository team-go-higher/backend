package gohigher.application.port.in;

import java.util.List;

import gohigher.application.Application;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KanbanApplicationResponse {

	private final String processType;
	private final List<KanbanApplicationDetailResponse> applications;

	public static KanbanApplicationResponse from(String processType, List<Application> applications) {
		List<KanbanApplicationDetailResponse> applicationDetailResponses = applications.stream()
			.map(KanbanApplicationDetailResponse::from)
			.toList();
		return new KanbanApplicationResponse(processType, applicationDetailResponses);
	}
}
