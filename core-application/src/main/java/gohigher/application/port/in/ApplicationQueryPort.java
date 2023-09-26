package gohigher.application.port.in;

import java.util.List;

public interface ApplicationQueryPort {

	ApplicationResponse findById(Long userId, Long applicationId);

	List<CalendarApplicationResponse> findByMonth(CalendarApplicationRequest request);

	List<DateApplicationResponse> findByDate(DateApplicationRequest request);

	PagingResponse<UnscheduledApplicationResponse> findUnscheduled(Long userId, PagingRequest request);

	List<KanbanApplicationResponse> findForKanban(Long userId);
}
