package gohigher.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.Application;
import gohigher.application.ApplicationErrorCode;
import gohigher.application.port.in.ApplicationQueryPort;
import gohigher.application.port.in.ApplicationResponse;
import gohigher.application.port.in.CalendarApplicationRequest;
import gohigher.application.port.in.CalendarApplicationResponse;
import gohigher.application.port.in.DateApplicationRequest;
import gohigher.application.port.in.DateApplicationResponse;
import gohigher.application.port.in.KanbanApplicationResponse;
import gohigher.application.port.in.KanbanByProcessApplicationResponse;
import gohigher.application.port.in.MyApplicationRequest;
import gohigher.application.port.in.MyApplicationResponse;
import gohigher.application.port.in.PagingRequest;
import gohigher.application.port.in.PagingResponse;
import gohigher.application.port.in.ProcessResponse;
import gohigher.application.port.in.UnscheduledApplicationResponse;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.search.ApplicationSortingType;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import gohigher.global.exception.GoHigherException;
import gohigher.pagination.PagingContainer;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicationQueryService implements ApplicationQueryPort {

	private final ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Override
	public PagingResponse<MyApplicationResponse> findAllByUserId(Long userId, PagingRequest pagingRequest,
		MyApplicationRequest request) {
		PagingContainer<Application> pagingContainer = applicationPersistenceQueryPort.findAllByUserId(
			userId, pagingRequest.getPage(), pagingRequest.getSize(),
			ApplicationSortingType.from(request.getSort()),
			ProcessType.from(request.getProcess()), request.getCompleted(), request.getCompanyName(),
			LocalDateTime.now());
		List<MyApplicationResponse> responses = findApplicationsByUserId(pagingContainer.getContent(),
			MyApplicationResponse::of);
		return new PagingResponse<>(pagingContainer.hasNext(), responses);
	}

	@Override
	public ApplicationResponse findById(Long userId, Long applicationId) {
		Application application = applicationPersistenceQueryPort.findByIdAndUserId(applicationId, userId)
			.orElseThrow(() -> new GoHigherException(ApplicationErrorCode.APPLICATION_NOT_FOUND));

		return ApplicationResponse.from(application);
	}

	@Override
	public List<CalendarApplicationResponse> findByMonth(CalendarApplicationRequest request) {
		List<Application> applications = applicationPersistenceQueryPort.findByUserIdAndMonth(request.getUserId(),
			request.getYear(), request.getMonth());

		return applications.stream()
			.flatMap(this::extractCalendarResponses)
			.toList();
	}

	@Override
	public List<DateApplicationResponse> findByDate(DateApplicationRequest request) {
		List<Application> applications = applicationPersistenceQueryPort.findByUserIdAndDate(request.getUserId(),
			request.getDate());
		return applications.stream()
			.flatMap(this::extractDateApplicationResponses)
			.toList();
	}

	@Override
	public PagingResponse<UnscheduledApplicationResponse> findUnscheduled(Long userId, PagingRequest request) {
		PagingContainer<Application> pagingContainer = applicationPersistenceQueryPort.findUnscheduledByUserId(
			userId, request.getPage(), request.getSize());
		List<UnscheduledApplicationResponse> responses = findApplicationsByUserId(pagingContainer.getContent(),
			UnscheduledApplicationResponse::of);
		return new PagingResponse<>(pagingContainer.hasNext(), responses);
	}

	@Override
	public List<KanbanApplicationResponse> findForKanban(Long userId) {
		List<Application> applications = applicationPersistenceQueryPort.findOnlyWithCurrentProcessByUserId(userId);
		return createKanbanApplicationResponses(applications);
	}

	@Override
	public List<KanbanByProcessApplicationResponse> findForKanbanByProcess(Long userId, ProcessType processType) {
		return applicationPersistenceQueryPort.findOnlyCurrentProcessByUserIdAndProcessType(userId, processType)
			.stream()
			.map(KanbanByProcessApplicationResponse::from)
			.toList();
	}

	private <T> List<T> findApplicationsByUserId(List<Application> applications,
		BiFunction<Application, Process, T> responseMapper) {
		return applications.stream()
			.flatMap(application -> extractApplicationResponse(application, responseMapper))
			.toList();
	}

	private <T> Stream<T> extractApplicationResponse(Application application,
		BiFunction<Application, Process, T> responseMapper) {
		return application.getProcesses()
			.stream()
			.map(process -> responseMapper.apply(application, process));
	}

	private Stream<CalendarApplicationResponse> extractCalendarResponses(Application application) {
		return application.getProcesses()
			.stream()
			.map(process -> CalendarApplicationResponse.of(application, process));
	}

	private Stream<DateApplicationResponse> extractDateApplicationResponses(Application application) {
		return application.getProcesses()
			.stream()
			.map(ProcessResponse::from)
			.map(processResponse -> DateApplicationResponse.of(application, processResponse));
	}

	private List<KanbanApplicationResponse> createKanbanApplicationResponses(List<Application> applications) {
		Map<ProcessType, List<Application>> groupedApplications = groupByProcessType(applications);
		return groupedApplications.entrySet()
			.stream()
			.map(process -> KanbanApplicationResponse.from(process.getKey().name(), process.getValue()))
			.toList();
	}

	private Map<ProcessType, List<Application>> groupByProcessType(List<Application> applications) {
		return applications.stream()
			.collect(Collectors.groupingBy(application -> application.getCurrentProcess().getType()));
	}
}
