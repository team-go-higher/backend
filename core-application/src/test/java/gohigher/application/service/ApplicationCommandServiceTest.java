package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;
import static gohigher.application.ApplicationFixture.NAVER_APPLICATION;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.application.port.in.CurrentProcessUpdateRequest;
import gohigher.application.port.out.persistence.ApplicationPersistenceCommandPort;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;

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
				when(applicationPersistenceQueryPort.findByIdAndUserId(applicationId, notExistUserId))
					.thenReturn(Optional.empty());

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
				when(applicationPersistenceQueryPort.findByIdAndUserId(notExistApplicationId, userId))
					.thenReturn(Optional.empty());

				// when & then
				assertThatThrownBy(() -> applicationCommandService.deleteApplication(userId, notExistApplicationId))
					.hasMessage(APPLICATION_NOT_FOUND.getMessage());
			}
		}

		// todo: 삭제된 지원서 <- 쿼리문 수정하고 나중에 추가

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
				when(applicationPersistenceQueryPort.findByIdAndUserId(applicationId, userId))
					.thenReturn(Optional.of(NAVER_APPLICATION.toDomain()));
				doNothing().when(applicationPersistenceCommandPort)
						.delete(applicationId);

				// when & then
				assertThatCode(() -> applicationCommandService.deleteApplication(userId, applicationId))
					.doesNotThrowAnyException();
			}
		}
	}
}
