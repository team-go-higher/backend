package gohigher.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Process 클래스의")
class ProcessTest {

	@DisplayName("makeFirstByType 메서드는")
	@Nested
	class Describe_makeFirstByType {

		@DisplayName("세부 전형을 입력받으면")
		@Nested
		class Context_input_description {

			@DisplayName("order가 1인 Process를 반환한다")
			@Test
			void it_returns_first_order_process() {
				// given & when
				Process process = Process.makeFirstByType(ProcessType.INTERVIEW, "1차 면접");

				// then
				assertThat(process.getOrder()).isEqualTo(1);
			}
		}
	}
}
