package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;
import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.application.Application;
import gohigher.application.port.in.CompletedUpdatingRequest;
import gohigher.application.port.in.CompletedUpdatingResponse;
import gohigher.application.port.in.CurrentProcessUpdateRequest;
import gohigher.application.port.out.persistence.ApplicationPersistenceCommandPort;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import gohigher.common.Process;
import gohigher.global.exception.GoHigherException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationCommandService 클래스의 ")
class ApplicationCommandServiceTest {

	private static final long APPLICATION_ID = 1L;
	private final long applicationOwnerId = 1L;

	@Mock
	private ApplicationPersistenceCommandPort applicationPersistenceCommandPort;

	@Mock
	private ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Mock
	private ApplicationProcessPersistenceQueryPort applicationProcessPersistenceQueryPort;

	@InjectMocks
	private ApplicationCommandService applicationCommandService;

	@DisplayName("UpdateCurrentProcess 메서드는")
	@Nested
	class Describe_UpdateCurrentProcess {

		private final long processId = 2L;
		private final long userId = 1L;

		@DisplayName("존재하지 않는 지원서의 현재 절차를 변경하려고 할 때")
		@Nested
		class NotFoundApplication {

			CurrentProcessUpdateRequest request = new CurrentProcessUpdateRequest(APPLICATION_ID, processId);

			@BeforeEach
			void setUp() {
				when(applicationPersistenceQueryPort.existsByIdAndUserId(APPLICATION_ID, userId))
					.thenReturn(false);
			}

			@DisplayName("예외를 발생시켜야 한다.")
			@Test
			void updateCurrentProcess_ApplicationNotFoundException() {
				//when then
				assertThatThrownBy(() -> applicationCommandService.updateCurrentProcess(userId, request))
					.hasMessage(APPLICATION_NOT_FOUND.getMessage());
			}
		}

		@DisplayName("존재하지 않는 절차 id로 현재 절차를 변경하려고 할 때")
		@Nested
		class ApplicationProcessNotFound {

			private final CurrentProcessUpdateRequest request =
				new CurrentProcessUpdateRequest(APPLICATION_ID, processId);

			@BeforeEach
			void setUp() {
				when(applicationPersistenceQueryPort.existsByIdAndUserId(APPLICATION_ID, userId))
					.thenReturn(true);
				when(applicationProcessPersistenceQueryPort.existsById(processId))
					.thenReturn(false);
			}

			@DisplayName("예외를 발생시킨다.")
			@Test
			void updateCurrentProcess() {
				//when then
				assertThatThrownBy(() -> applicationCommandService.updateCurrentProcess(applicationOwnerId, request))
					.hasMessage(APPLICATION_PROCESS_NOT_FOUND.getMessage());
			}
		}

		@DisplayName("자신의 지원서의 현재 절차를 변경하려고 할 때")
		@Nested
		class ApplicationCurrentProcessUpdate {

			private final CurrentProcessUpdateRequest request =
				new CurrentProcessUpdateRequest(APPLICATION_ID, processId);

			@BeforeEach
			void setUp() {
				when(applicationPersistenceQueryPort.existsByIdAndUserId(APPLICATION_ID, userId))
					.thenReturn(true);
				when(applicationProcessPersistenceQueryPort.existsById(processId))
					.thenReturn(true);
			}

			@DisplayName("정상적으로 현재 절차를 변경할 수 있어야 한다.")
			@Test
			void updateCurrentProcess() {
				//when
				applicationCommandService.updateCurrentProcess(applicationOwnerId, request);

				//then
				verify(applicationPersistenceCommandPort)
					.updateCurrentProcessOrder(request.getApplicationId(), userId, request.getProcessId());
			}
		}
	}

	@DisplayName("deleteApplication 메서드는")
	@Nested
	class Describe_deleteApplication {

		@DisplayName("존재하지 않는 지원서의 아이디로 요청하면")
		@Nested
		class Context_with_not_exist_application_id {

			@DisplayName("예외가 발생한다")
			@Test
			void it_throw_exception() {
				// given
				long notExistUserId = 0L;
				long applicationId = 1L;

				// mocking
				when(applicationPersistenceQueryPort.existsByIdAndUserId(applicationId, notExistUserId))
					.thenReturn(false);

				// when & then
				assertThatThrownBy(() -> applicationCommandService.deleteApplication(notExistUserId, applicationId))
					.hasMessage(APPLICATION_NOT_FOUND.getMessage());
			}
		}

		@DisplayName("다른 사용자가 작성한 지원서의 아이디로 요청하면")
		@Nested
		class Context_with_written_other_user {

			@DisplayName("예외가 발생한다")
			@Test
			void it_throw_exception() {
				// given
				long userId = 1L;
				long notExistApplicationId = 0L;

				// mocking
				when(applicationPersistenceQueryPort.existsByIdAndUserId(notExistApplicationId, userId))
					.thenReturn(false);

				// when & then
				assertThatThrownBy(() -> applicationCommandService.deleteApplication(userId, notExistApplicationId))
					.hasMessage(APPLICATION_NOT_FOUND.getMessage());
			}
		}

		@DisplayName("지원서의 아이디로")
		@Nested
		class Context_with_application_id {

			@DisplayName("지원서를 삭제한다")
			@Test
			void it_delete_application() {
				// given
				long userId = 1L;
				long applicationId = 2L;

				// mocking
				when(applicationPersistenceQueryPort.existsByIdAndUserId(applicationId, userId))
					.thenReturn(true);
				doNothing().when(applicationPersistenceCommandPort)
					.delete(applicationId);

				// when & then
				assertThatCode(() -> applicationCommandService.deleteApplication(userId, applicationId))
					.doesNotThrowAnyException();
			}
		}
	}

	@DisplayName("지원서의 완료 여부를 변경하려 할 때,")
	@Nested
	class UpdateVisible {
		private final Long applicationId = 1L;
		private final Long userId = 1L;
		private final int year = 2023;
		private final int month = 9;

		private Application application;

		@BeforeEach
		void setUp() {
			LocalDate searchDate = LocalDate.of(year, month, 20);
			Process toApply = TO_APPLY.toDomainWithSchedule(searchDate);
			Process document = DOCUMENT.toDomainWithSchedule(searchDate);
			Process interview = INTERVIEW.toDomainWithSchedule(searchDate);

			List<Process> naverProcesses = List.of(toApply, document, interview);
			application = NAVER_APPLICATION.toPersistedDomain(applicationId, naverProcesses, toApply);
		}

		@DisplayName("정상적으로 변경할 수 있어야한다.")
		@Test
		void success() {
			// given
			when(applicationPersistenceQueryPort.findByIdAndUserId(applicationId, userId))
				.thenReturn(Optional.of(application));
			doNothing().when(applicationPersistenceCommandPort)
				.updateCompleted(application);
			CompletedUpdatingRequest request = new CompletedUpdatingRequest(true);

			// when
			CompletedUpdatingResponse response = applicationCommandService.updateCompleted(userId,
				applicationId, request);

			// then
			assertThat(response.getIsCompleted()).isEqualTo(request.getIsCompleted());
		}

		@DisplayName("기존 상태로의 변경을 요청한다면 예외를 발생한다.")
		@Test
		void fail_if_request_to_existing_state() {
			// given
			when(applicationPersistenceQueryPort.findByIdAndUserId(applicationId, userId))
				.thenReturn(Optional.of(application));
			CompletedUpdatingRequest request = new CompletedUpdatingRequest(application.isCompleted());

			// when & then
			assertThatThrownBy(
				() -> applicationCommandService.updateCompleted(userId, applicationId, request)).isInstanceOf(
				GoHigherException.class).hasMessage(ALREADY_VISIBLE_STATE_TO_CHANGE.getMessage());
		}
	}
}
