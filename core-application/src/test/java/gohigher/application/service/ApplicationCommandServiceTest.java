package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;
import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.application.ApplicationFixture;
import gohigher.application.ProcessFixture;
import gohigher.application.port.in.CurrentProcessUpdateRequest;
import gohigher.application.port.in.SpecificApplicationUpdateProcessRequest;
import gohigher.application.port.in.SpecificApplicationUpdateRequest;
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
					.updateCurrentProcessOrder(request.getApplicationId(), request.getProcessId());
			}
		}
	}

	@DisplayName("updateSpecifically 메서드는")
	@Nested
	class Describe_updateSpecifically {

		private final Long userId = 1L;
		private final Long applicationId = 1L;
		private SpecificApplicationUpdateRequest request;

		@DisplayName("존재하는 지원서에 요청을 할 경우")
		@Nested
		class Context_exist_application {

			@BeforeEach
			void setUp() {
				request = convertToRequest(applicationId, NAVER_APPLICATION, TO_APPLY, DOCUMENT, INTERVIEW);

				when(applicationPersistenceQueryPort.existsByIdAndUserId(APPLICATION_ID, userId))
					.thenReturn(true);
			}

			@DisplayName("정상적으로 업데이트를 수행한다.")
			@Test
			public void it_update_application() {
				applicationCommandService.updateSpecifically(userId, request);

				verify(applicationPersistenceCommandPort).update(any());
			}
		}

		@DisplayName("존재하지 않는 지원서에 요청을 할 경우")
		@Nested
		class Context_not_exist_application {

			@BeforeEach
			void setUp() {
				request = convertToRequest(applicationId, NAVER_APPLICATION, TO_APPLY, DOCUMENT, INTERVIEW);

				when(applicationPersistenceQueryPort.existsByIdAndUserId(APPLICATION_ID, userId))
					.thenReturn(false);
			}

			@DisplayName("예외를 발생시킨다.")
			@Test
			public void it_throws_exception() {
				assertThatThrownBy(() -> applicationCommandService.updateSpecifically(applicationOwnerId, request))
					.hasMessage(APPLICATION_NOT_FOUND.getMessage());
			}
		}

		private SpecificApplicationUpdateRequest convertToRequest(Long applicationId,
			ApplicationFixture applicationFixture, ProcessFixture... processesFixtures) {
			List<SpecificApplicationUpdateProcessRequest> processes = Arrays.stream(processesFixtures)
				.map(p -> new SpecificApplicationUpdateProcessRequest(null, p.getType().name(), p.getDescription(),
					p.getSchedule()))
				.toList();

			return new SpecificApplicationUpdateRequest(
				applicationId,
				applicationFixture.getCompanyName(),
				applicationFixture.getTeam(),
				applicationFixture.getLocation(),
				applicationFixture.getContact(),
				applicationFixture.getPosition(),
				applicationFixture.getSpecificPosition(),
				applicationFixture.getJobDescription(),
				applicationFixture.getWorkType(),
				applicationFixture.getEmploymentType().name(),
				applicationFixture.getCareerRequirement(),
				applicationFixture.getRequiredCapability(),
				applicationFixture.getPreferredQualification(),
				processes,
				applicationFixture.getUrl(),
				0
			);
		}
	}
}
