package gohigher.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gohigher.common.ProcessType;

@DisplayName("CurrentProcess 클래스의")
class CurrentProcessTest {

	@DisplayName("생성자는")
	@Nested
	class Describe_constructor {

		@DisplayName("process type 이 null 일 경우")
		@Nested
		class Context_process_type_is_null {

			@DisplayName("to apply 로 변경한다")
			@Test
			void it_replace_to_apply() {
				// given
				ProcessType processType = null;

				// when
				CurrentProcess currentProcess = new CurrentProcess(
					1L, "회사명", "직무", "상세 직무", processType,
					"설명", LocalDateTime.now()
				);

				// then
				assertThat(currentProcess.getType()).isEqualTo(ProcessType.TO_APPLY);
			}
		}

		@DisplayName("process type 이 null 이 아닐 경우")
		@Nested
		class Context_process_type_is_not_null {

			@DisplayName("받아온 그대로 저장한다")
			@Test
			void it_not_replace() {
				// given
				ProcessType processType = ProcessType.DOCUMENT;

				// when
				CurrentProcess currentProcess = new CurrentProcess(
					1L, "회사명", "직무", "상세 직무", processType,
					"설명", LocalDateTime.now()
				);

				// then
				assertThat(currentProcess.getType()).isEqualTo(processType);
			}
		}
	}
}
