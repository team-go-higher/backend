package gohigher.application.port.in;

import java.util.List;

import gohigher.application.Application;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KanbanApplicationResponse {

	private final String processType;
	private final List<KanbanByProcessApplicationResponse> applications;

	public static KanbanApplicationResponse from(String processType, List<Application> applications) {
		return new KanbanApplicationResponse(
			processType,
			applications.stream()
				.map(KanbanByProcessApplicationResponse::from)
				.toList()
		);
	}
}
