package gohigher.common;

import static gohigher.application.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Processes 클래스의")
class ProcessesTest {

	@DisplayName("of 정적 팩터리 메소드는")
	@Nested
	class Describe_of {

		@DisplayName("중복된 ProcessType의 Process들을 받을 경우")
		@Nested
		class Context_with_processes_that_duplicated_process_type {

			private final Process test1 = TEST.toDomainWithoutOrder();
			private final Process test2 = CODING_TEST.toDomainWithoutOrder();
			private final Process interview1 = FIRST_INTERVIEW.toDomainWithoutOrder();
			private final Process interview2 = SECOND_INTERVIEW.toDomainWithoutOrder();
			private final List<Process> input = List.of(test1, test2, interview1, interview2);

			@DisplayName("ProcessType별로 분리하여 저장된 Processes객체를 반환한다.")
			@Test
			void it_returns_processes_that_assigned_order() {
				Processes processes = Processes.of(input);

				assertAll(
					() -> assertThat(processes.getValues().get(ProcessType.TEST)).contains(test1, test2),
					() -> assertThat(processes.getValues().get(ProcessType.INTERVIEW)).contains(interview1, interview2)
				);
			}
		}
	}

	@DisplayName("initiallyFrom 정적 팩터리 메소드는")
	@Nested
	class Describe_initiallyFrom {

		@DisplayName("Process들을 받을 경우")
		@Nested
		class Context_with_processes_that_not_assigned_order {

			private final Process test1 = TEST.toDomainWithoutOrder();
			private final Process test2 = CODING_TEST.toDomainWithoutOrder();
			private final Process interview1 = FIRST_INTERVIEW.toDomainWithoutOrder();
			private final Process interview2 = SECOND_INTERVIEW.toDomainWithoutOrder();
			private final List<Process> input = List.of(test1, test2, interview1, interview2);

			@DisplayName("ProcessType별로 Order를 1번부터 순차적으로 할당한 Processes객체를 반환한다.")
			@Test
			void it_returns_processes_that_assigned_order() {
				Processes processes = Processes.initialFrom(input);
				List<Process> testProcesses = processes.getValues().get(ProcessType.TEST);
				List<Process> interviewProcesses = processes.getValues().get(ProcessType.INTERVIEW);

				assertAll(
					() -> assertThat(testProcesses.get(0).getOrder()).isEqualTo(1),
					() -> assertThat(testProcesses.get(1).getOrder()).isEqualTo(2),
					() -> assertThat(interviewProcesses.get(0).getOrder()).isEqualTo(1),
					() -> assertThat(interviewProcesses.get(1).getOrder()).isEqualTo(2)
				);
			}
		}
	}
}
