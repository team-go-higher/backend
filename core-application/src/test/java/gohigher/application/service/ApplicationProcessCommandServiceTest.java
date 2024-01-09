package gohigher.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.application.ApplicationErrorCode;
import gohigher.application.port.in.ApplicationProcessByProcessTypeResponse;
import gohigher.application.port.in.UnscheduledProcessRequest;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceCommandPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;
import gohigher.common.Process;
import gohigher.common.ProcessType;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationProcessCommandService 클래스의 ")
class ApplicationProcessCommandServiceTest {

	@Mock
	private ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Mock
	private ApplicationProcessPersistenceCommandPort applicationProcessPersistenceCommandPort;

	@Mock
	private ApplicationProcessPersistenceQueryPort applicationProcessPersistenceQueryPort;

	@InjectMocks
	private ApplicationProcessCommandService applicationProcessCommandService;

	@DisplayName("register 메서드는")
	@Nested
	class Describe_Register {

		Long userId = 1L;
		Long applicationId = 1L;

		@DisplayName("새로운 전형 정보를 받았을 때,")
		@Nested
		class Context_Request_New_Process {

			@DisplayName("기존에 해당 타입의 전형이 없다면 전형 정보를 저장하고 값을 리턴한다.")
			@Test
			void it_returns_saved_process() {
				// given
				ProcessType interview = ProcessType.INTERVIEW;
				when(applicationPersistenceQueryPort.existsByIdAndUserId(userId, applicationId)).thenReturn(true);
				when(applicationProcessPersistenceQueryPort.findByApplicationIdAndProcessType(applicationId, interview))
					.thenReturn(new ArrayList<>());
				UnscheduledProcessRequest interviewRequest = new UnscheduledProcessRequest(interview.name(),
					"1차 면접");
				when(applicationProcessPersistenceCommandPort.save(anyLong(), any())).thenReturn(
					new Process(1L, interview, interviewRequest.getDescription(),
						null, 1));

				// when
				ApplicationProcessByProcessTypeResponse response = applicationProcessCommandService.register(userId,
					applicationId, interviewRequest);

				// then
				assertThat(response.getDescription()).isEqualTo(interviewRequest.getDescription());
			}
		}

		@DisplayName("지원서 아이디와 사용자 아이디가 일치하는 지원서가 없을 때")
		@Nested
		class Context_not_exist_application_with_application_id_and_user_id {

			@DisplayName("예외가 발생한다")
			@Test
			void it_throw_exception() {
				// given
				long notExistUserId = 0L;
				long notExistApplicationId = 0L;
				UnscheduledProcessRequest request = mock(UnscheduledProcessRequest.class);

				// mocking
				when(applicationPersistenceQueryPort.existsByIdAndUserId(notExistApplicationId, notExistUserId))
					.thenReturn(false);

				// when & then
				assertThatThrownBy(() -> applicationProcessCommandService.register(notExistUserId, notExistApplicationId, request))
					.hasMessage(ApplicationErrorCode.APPLICATION_NOT_FOUND.getMessage());
			}
		}
	}
}
