package gohigher.application;

import static gohigher.application.ApplicationErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gohigher.common.EmploymentType;
import gohigher.common.Process;
import gohigher.common.ProcessType;

@DisplayName("Application 클래스는")
class ApplicationTest {

	@Nested
	@DisplayName("getProcessTypeOf 메서드는")
	class Describe_GetProcessTypeOf {

		private final Long USER_ID = 1L;
		private final Process firstProcess = new Process(ProcessType.TEST, "코딩테스트", LocalDateTime.now());
		private final Process secondProcess = new Process(ProcessType.INTERVIEW, "기술 면접", LocalDateTime.now());
		private final Process thirdProcess = new Process(ProcessType.INTERVIEW, "인성 면접", LocalDateTime.now());
		private final Application application = new Application("", "", "", "", "", "", "", "",
			EmploymentType.PERMANENT, "", "", "", LocalDateTime.now(),
			List.of(firstProcess, secondProcess, thirdProcess), "", USER_ID, firstProcess);

		@Nested
		@DisplayName("해당 지원서에 존재하지 않는 전형 타입의 전형을 찾으려 할 때,")
		class ExistentProcessOfProcessType {

			@DisplayName("예외를 발생시킨다.")
			@Test
			void getProcessType() {
				//given
				ProcessType processType = ProcessType.INTERVIEW;

				//when
				Process actual = application.getProcessOfType(processType);

				//then
				assertThat(actual).isEqualTo(secondProcess);
			}
		}

		@Nested
		@DisplayName("해당 지원서에 존재하지 않는 전형 타입의 전형을 찾으려 할 때,")
		class NotFoundProcessOfProcessType {

			@DisplayName("예외를 발생시킨다.")
			@Test
			void getProcessType_ApplicationProcessNotFound() {
				//given
				ProcessType notFoundProcessType = ProcessType.TO_APPLY;

				//when then
				assertThatThrownBy(() -> application.getProcessOfType(notFoundProcessType))
					.hasMessage(APPLICATION_PROCESS_NOT_FOUND.getMessage());
			}
		}
	}
}
