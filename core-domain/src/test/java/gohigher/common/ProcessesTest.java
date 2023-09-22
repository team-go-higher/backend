package gohigher.common;

import static gohigher.application.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Process 클래스의")
class ProcessesTest {

	@DisplayName("initiallyFrom 정적 팩터리 메소드는")
	@Nested
	class Describe_initiallyFrom {

		@DisplayName("order가 할당되지 않은 Process들을 받을 경우")
		@Nested
		class Context_with_processes_that_not_assigned_order {

			private final Process toApply = TO_APPLY.toDomainWithoutOrder();
			private final Process document = DOCUMENT.toDomainWithoutOrder();
			private final Process test = TEST.toDomainWithoutOrder();
			private final Process interview = INTERVIEW.toDomainWithoutOrder();
			private final List<Process> input = List.of(toApply, document, test, interview);

			@DisplayName("Order를 순차적으로 할당하며 생성한다.")
			@Test
			void it_returns_processes_that_assigned_order() {
				Processes processes = Processes.initiallyFrom(input);

				assertAll(
					() -> assertThat(processes.getValues().get(0).getOrder()).isEqualTo(0),
					() -> assertThat(processes.getValues().get(1).getOrder()).isEqualTo(1),
					() -> assertThat(processes.getValues().get(2).getOrder()).isEqualTo(2),
					() -> assertThat(processes.getValues().get(3).getOrder()).isEqualTo(3)
				);
			}
		}

		@DisplayName("비어있는 Process리스트를 받을 경우")
		@Nested
		class Context_with_empty_processes {

			private final List<Process> input = new ArrayList<>();

			@DisplayName("지원예정 과정이 추가된 과정리스트를 반환한다.")
			@Test
			void it_returns_processes_with_default_process() {
				Processes processes = Processes.initiallyFrom(input);

				assertAll(
					() -> assertThat(processes.getValues()).hasSize(1),
					() -> assertThat(processes.getValues().get(0).getType()).isEqualTo(ProcessType.TO_APPLY)
				);
			}
		}
	}
}
