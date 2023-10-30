package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;
import static gohigher.common.ProcessType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.application.port.out.persistence.ApplicationProcessPersistenceQueryPort;

@DisplayName("ApplicationProcessQueryService 클래스의")
@ExtendWith(MockitoExtension.class)
class ApplicationProcessQueryServiceTest {

	@Mock
	private ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	@Mock
	private ApplicationProcessPersistenceQueryPort applicationProcessPersistenceQueryPort;

	@InjectMocks
	private ApplicationProcessQueryService applicationProcessQueryService;

	@DisplayName("findByApplicationIdAndProcessType 메서드는")
	@Nested
	class Describe_FindByApplicationIdAndProcessType {

		private final Long userId = 2L;

		@DisplayName("id와 userId가 일치하는 지원서가 없는 경우에")
		@Nested
		class Context_NonExistent_Application_By_Id_And_User_Id {

			private final Long applicationId = 3L;

			@BeforeEach
			void setUp() {
				when(applicationPersistenceQueryPort.existsByIdAndUserId(applicationId, userId))
					.thenReturn(false);
			}

			@DisplayName("예외를 발생시킨다.")
			@Test
			void it_Throws_Application_Not_Found_Exception() {
				//when then
				assertThatThrownBy(() ->
					applicationProcessQueryService.findByApplicationIdAndProcessType(userId, applicationId, TEST))
					.hasMessage(APPLICATION_NOT_FOUND.getMessage());
			}
		}
	}
}
