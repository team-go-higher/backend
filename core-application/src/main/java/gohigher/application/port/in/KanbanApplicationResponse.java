package gohigher.application.port.in;

import java.util.List;

import gohigher.application.Application;
import gohigher.pagination.PagingContainer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KanbanApplicationResponse {

	private final String processType;
	private final PagingResponse<KanbanApplicationDetailResponse> applications;

	public static KanbanApplicationResponse from(String processType, PagingContainer<Application> applications) {
		List<KanbanApplicationDetailResponse> applicationDetailResponses = applications.getContent()
			.stream()
			.map(KanbanApplicationDetailResponse::from)
			.toList();
		return new KanbanApplicationResponse(
			processType,
			new PagingResponse<>(
				applications.hasNext(),
				applicationDetailResponses
			)
		);
	}
}
