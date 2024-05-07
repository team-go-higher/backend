package gohigher.application.port.in;

import java.util.List;

import gohigher.common.ProcessType;

public interface ApplicationQueryPort {

	PagingResponse<MyApplicationResponse> findAllByUserId(Long userId, PagingRequest pagingRequest, MyApplicationRequest request);

	ApplicationResponse findById(Long userId, Long applicationId);

	List<CalendarApplicationResponse> findByMonth(CalendarApplicationRequest request);

	List<DateApplicationResponse> findByDate(DateApplicationRequest request);

	PagingResponse<UnscheduledApplicationResponse> findUnscheduled(Long userId, PagingRequest request);

	List<KanbanApplicationResponse> findForKanban(Long userId);

	List<KanbanByProcessApplicationResponse> findForKanbanByProcess(Long userId, ProcessType processType);
}
