package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;
import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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
import gohigher.application.port.in.CurrentProcessUpdateRequest;
import gohigher.application.port.out.persistence.ApplicationPersistenceCommandPort;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import gohigher.common.ProcessType;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationCommandService 클래스의 ")
class ApplicationCommandServiceTest {

	private static final long APPLICATION_ID = 1L;

	@Mock
	private ApplicationPersistenceCommandPort applicationPersistenceCommandPort;
	@Mock
	private ApplicationPersistenceQueryPort applicationPersistenceQueryPort;
	@Mock
	private ApplicationProcessPersistenceQueryPort applicationProcessPersistenceQueryPort;
	@InjectMocks
	private ApplicationCommandService applicationCommandService;

	private final Process firstProcess = TO_APPLY.toDomain();
	private final Process secondProcess = DOCUMENT.toDomain();
	private final Application application = NAVER_APPLICATION.toDomain(List.of(firstProcess, secondProcess), firstProcess);

	private final long applicationOwnerId = 1L;

	@DisplayName("UpdateCurrentProcess 메서드는")
	@Nested
	class Describe_UpdateCurrentProcess {

		private final long processId = 1L;

		@DisplayName("존재하지 않는 지원서의 현재 절차를 변경하려고 할 때")
		@Nested
		class NotFoundApplication {

			private final long userId = 1L;
			CurrentProcessUpdateRequest request = new CurrentProcessUpdateRequest(APPLICATION_ID, processId);

			@BeforeEach
			void setUp() {
				when(applicationPersistenceQueryPort.findById(APPLICATION_ID))
					.thenReturn(Optional.empty());
			}

			@DisplayName("예외를 발생시켜야 한다.")
			@Test
			void updateCurrentProcess_ApplicationNotFoundException() {
				//when then
				assertThatThrownBy(() -> applicationCommandService.updateCurrentProcess(userId, request))
					.hasMessage(APPLICATION_NOT_FOUND.getMessage());
			}
		}

		@DisplayName("다른 사용자가 작성한 지원서의 현재 절차를 변경하려고 할 때")
		@Nested
		class ForbiddenApplicationCurrentProcessUpdate {

			private final CurrentProcessUpdateRequest request =
				new CurrentProcessUpdateRequest(APPLICATION_ID, processId);

			@BeforeEach
			void setUp() {
				when(applicationPersistenceQueryPort.findById(APPLICATION_ID))
					.thenReturn(Optional.of(application));
			}

			@DisplayName("예외를 발생시켜야 한다.")
			@Test
			void updateCurrentProcess_Forbidden() {
				//given
				long anotherUserId = 2L;

				//when then
				assertThatThrownBy(() -> applicationCommandService.updateCurrentProcess(anotherUserId, request))
					.hasMessage(APPLICATION_FORBIDDEN.getMessage());
			}
		}

		@DisplayName("존재하지 않는 절차 id로 현재 절차를 변경하려고 할 때")
		@Nested
		class ApplicationProcessNotFound {

			private final CurrentProcessUpdateRequest request =
				new CurrentProcessUpdateRequest(APPLICATION_ID, processId);

			@BeforeEach
			void setUp() {
				when(applicationPersistenceQueryPort.findById(APPLICATION_ID))
					.thenReturn(Optional.of(application));
				when(applicationProcessPersistenceQueryPort.findById(processId))
					.thenReturn(Optional.empty());
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
				Process process = new Process(1L, ProcessType.INTERVIEW, "기술면접", LocalDateTime.now());
				when(applicationPersistenceQueryPort.findById(APPLICATION_ID))
					.thenReturn(Optional.of(application));
				when(applicationProcessPersistenceQueryPort.findById(processId))
					.thenReturn(Optional.of(process));
			}

			@DisplayName("정상적으로 현재 절차를 변경할 수 있어야 한다.")
			@Test
			void updateCurrentProcess() {
				//when then
				assertThatCode(() -> applicationCommandService.updateCurrentProcess(applicationOwnerId, request))
					.doesNotThrowAnyException();
			}
		}
	}
}
