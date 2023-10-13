package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;
import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.application.Application;
import gohigher.application.port.in.CalendarApplicationRequest;
import gohigher.application.port.in.CalendarApplicationResponse;
import gohigher.application.port.in.DateApplicationRequest;
import gohigher.application.port.in.DateApplicationResponse;
import gohigher.application.port.in.KanbanApplicationResponse;
import gohigher.application.port.in.KanbanByProcessApplicationResponse;
import gohigher.application.port.in.PagingRequest;
import gohigher.application.port.in.PagingResponse;
import gohigher.application.port.in.UnscheduledApplicationResponse;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import gohigher.pagination.PagingContainer;

@DisplayName("ApplicationQueryService 클래스의")
@ExtendWith(MockitoExtension.class)
class ApplicationQueryServiceTest {

	@Mock
	private ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	private ApplicationQueryService applicationQueryService;

	@BeforeEach
	void setUp() {
		applicationQueryService = new ApplicationQueryService(applicationPersistenceQueryPort);
	}

	@DisplayName("findById 메서드는")
	@Nested
	class Describe_findById {

		private final long userId = 1L;

		@DisplayName("존재하는 지원서를 조회할 경우")
		@Nested
		class Context_with_exist_application {

			private final long applicationId = 1L;

			@BeforeEach
			void setUp() {
				Application naverApplication = NAVER_APPLICATION.toDomain();
				given(applicationPersistenceQueryPort.findByIdAndUserId(applicationId, userId))
					.willReturn(Optional.of(naverApplication));
			}

			@DisplayName("예외가 발생하지 않는다.")
			@Test
			void it_does_not_throws_exception() {
				assertThatCode(() -> applicationQueryService.findById(userId, applicationId))
					.doesNotThrowAnyException();
			}
		}

		@DisplayName("조회하는 지원서가 존재하지 않을 경우")
		@Nested
		class Context_with_not_exist_application {

			private final long notExistApplicationId = 1L;

			@BeforeEach
			void setUp() {
				given(applicationPersistenceQueryPort.findByIdAndUserId(notExistApplicationId, userId))
					.willReturn(Optional.empty());
			}

			@DisplayName("예외가 발생한다.")
			@Test
			void it_throws_exception() {
				assertThatThrownBy(() -> applicationQueryService.findById(userId, notExistApplicationId))
					.hasMessage(APPLICATION_NOT_FOUND.getMessage());
			}
		}
	}

	@DisplayName("findByIdAndMonth 메서드는")
	@Nested
	class Describe_findByIdAndMonth {

		private final long userId = 1L;
		private final int year = 2023;
		private final int month = 9;

		@DisplayName("해당 연/월에 유저가 등록한 지원서 일정이 정보가 있을 때")
		@Nested
		class Context_with_schedules {

			private final CalendarApplicationRequest request = new CalendarApplicationRequest(userId, year, month);
			private List<Process> naverProcesses;
			private List<Process> kakaoProcesses;

			@BeforeEach
			void setUp() {
				LocalDate searchDate = LocalDate.of(year, month, 20);
				Process toApply = TO_APPLY.toDomainWithSchedule(searchDate);
				Process document = DOCUMENT.toDomainWithSchedule(searchDate);
				Process interview = INTERVIEW.toDomainWithSchedule(searchDate);

				naverProcesses = List.of(toApply, document, interview);
				Application naverApplication = NAVER_APPLICATION.toDomain(naverProcesses, toApply);

				kakaoProcesses = List.of(toApply, document);
				Application kakaoApplication = KAKAO_APPLICATION.toDomain(kakaoProcesses, toApply);

				given(applicationPersistenceQueryPort.findByUserIdAndMonth(userId, year, month))
					.willReturn(List.of(naverApplication, kakaoApplication));
			}

			@DisplayName("일정 정보를 반환한다.")
			@Test
			void it_return_application_processes() {
				List<CalendarApplicationResponse> actual = applicationQueryService.findByMonth(request);

				assertThat(actual).hasSize(naverProcesses.size() + kakaoProcesses.size());
			}
		}
	}

	@DisplayName("findByDate 메서드는")
	@Nested
	class Describe_findByDate {

		private final long userId = 1L;
		private final LocalDate date = LocalDate.of(2023, 9, 12);

		@DisplayName("해당 날짜가 전형일인 지원서가 있을 때")
		@Nested
		class Context_with_schedules {

			Process interview = INTERVIEW.toDomainWithSchedule(date);
			Process test = TEST.toDomainWithSchedule(date);

			@DisplayName("일정 정보를 반환한다.")
			@Test
			void it_return_processes() {
				// given
				List<Process> naverProcesses = List.of(this.interview);
				Application naverApplication = NAVER_APPLICATION.toDomain(naverProcesses, interview);
				List<Process> kakaoProcesses = List.of(this.test);
				Application kakaoApplication = KAKAO_APPLICATION.toDomain(kakaoProcesses, test);
				given(applicationPersistenceQueryPort.findByUserIdAndDate(userId, date)).willReturn(
					List.of(naverApplication, kakaoApplication));

				// when
				List<DateApplicationResponse> responses = applicationQueryService.findByDate(
					new DateApplicationRequest(userId, "2023-09-12"));

				// then
				assertThat(responses).hasSize(naverProcesses.size() + kakaoProcesses.size());
			}
		}
	}

	@DisplayName("findUnscheduled 메서드는")
	@Nested
	class Describe_findUnscheduled {

		@DisplayName("전형일이 작성되어 있지 않은 프로세스들이 있을 떄")
		@Nested
		class Context_exist_processes_without_schedule {

			@DisplayName("해당 전형들을 포함한 어플리케이션을 반환한다")
			@Test
			void it_return_applications_with_process() {
				// given
				Long userId = 1L;

				int page = 1;
				int size = 10;
				PagingRequest request = new PagingRequest(page, size);

				Process process = TEST.toPersistedDomain(1);
				List<Application> applications = List.of(
					NAVER_APPLICATION.toPersistedDomain(1, List.of(process), process));
				given(applicationPersistenceQueryPort.findUnscheduledByUserId(userId, page, size))
					.willReturn(new PagingContainer<>(false, applications));

				// when
				PagingResponse<UnscheduledApplicationResponse> response = applicationQueryService.findUnscheduled(
					userId, request);

				// then
				assertThat(response.getContent()).hasSize(applications.size());
			}
		}
	}

	@DisplayName("findForKanban 메서드는")
	@Nested
	class Describe_findForKanban {

		@DisplayName("사용자 아이디에 해당하는 지원서 목록이 있을 때")
		@Nested
		class Context_with_user_id {

			@DisplayName("모든 전형 지원서 목록의 현재 전형 정보를 반환한다")
			@Test
			void it_return_application_processes() {
				// given
				Long userId = 1L;
				int page = 1;
				int size = 10;
				PagingRequest request = new PagingRequest(page, size);

				ProcessType processType = ProcessType.TO_APPLY;
				Process process = TO_APPLY.toDomain();
				Application application = NAVER_APPLICATION.toDomain(List.of(process), process);
				List<Application> applications = List.of(application);

				given(applicationPersistenceQueryPort.findOnlyCurrentProcessByUserIdAndProcessType(eq(userId), any(),
					eq(page), eq(size))).willReturn(new PagingContainer<>(false, applications));

				// when
				List<KanbanApplicationResponse> response = applicationQueryService.findForKanban(userId, request);

				// then
				Optional<String> processes = response.stream()
					.map(KanbanApplicationResponse::getProcessType)
					.filter(it -> it.equals(processType.name()))
					.findFirst();
				assertThat(processes).isNotEmpty();
			}
		}
	}

	@DisplayName("findForKanbanByProcess 메서드는")
	@Nested
	class Describe_findForKanbanByProcess {

		@DisplayName("사용자 아이디와 프로세스 타입에 해당하는 지원서 목록이 있을 때")
		@Nested
		class Context_with_user_id_and_process_type {

			@DisplayName("지원서들의 현재 전형 정보를 반환한다")
			@Test
			void it_return_application_processes() {
				// given
				Long userId = 1L;

				int page = 1;
				int size = 10;
				PagingRequest request = new PagingRequest(page, size);
				ProcessType processType = ProcessType.TO_APPLY;

				Process process = TO_APPLY.toPersistedDomain(1);
				List<Application> applications = List.of(
					NAVER_APPLICATION.toPersistedDomain(1, List.of(process), process));
				given(applicationPersistenceQueryPort.findOnlyCurrentProcessByUserIdAndProcessType(userId, processType,
					page, size)).willReturn(new PagingContainer<>(false, applications));

				// when
				PagingResponse<KanbanByProcessApplicationResponse> response = applicationQueryService.findForKanbanByProcess(
					userId, processType, request);

				// then
				assertThat(response.getContent()).hasSize(applications.size());
			}
		}
	}
}
