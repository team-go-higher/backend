package gohigher.common;

import static gohigher.application.ProcessFixture.TEST;
import static gohigher.application.ProcessFixture.*;
import static gohigher.common.ProcessType.DOCUMENT;
import static gohigher.common.ProcessType.TO_APPLY;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import gohigher.application.ProcessFixture;

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

		@DisplayName("Process 리스트를 받을 경우")
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
					() -> assertThat(testProcesses).containsExactly(test1, test2),
					() -> assertThat(testProcesses.get(0).getOrder()).isEqualTo(1),
					() -> assertThat(testProcesses.get(1).getOrder()).isEqualTo(2),
					() -> assertThat(interviewProcesses).containsExactly(interview1, interview2),
					() -> assertThat(interviewProcesses.get(0).getOrder()).isEqualTo(1),
					() -> assertThat(interviewProcesses.get(1).getOrder()).isEqualTo(2)
				);
			}
		}

		@DisplayName("1개의 지원 예정 혹은 서류 전형이 포함된 Process 리스트를 받을 경우")
		@Nested
		class Context_with_processes_that_includes_one_toApply_or_document_process {

			private final Process test1 = TEST.toDomainWithoutOrder();
			private final Process test2 = CODING_TEST.toDomainWithoutOrder();
			private final Process interview1 = FIRST_INTERVIEW.toDomainWithoutOrder();
			private final Process interview2 = SECOND_INTERVIEW.toDomainWithoutOrder();
			private final List<Process> input = new ArrayList<>(List.of(test1, test2, interview1, interview2));

			@DisplayName("동일한 일자의 지원 예정, 서류 전형을 추가한 Processes객체를 반환한다.")
			@ParameterizedTest
			@EnumSource(value = ProcessType.class, names = {"TO_APPLY", "DOCUMENT"})
			void it_returns_processes_that_assigned_order(ProcessType processType) {
				Process process = new Process(null, processType, "세부직무", LocalDateTime.now());
				input.add(process);

				Processes processes = Processes.initialFrom(input);
				Map<ProcessType, List<Process>> values = processes.getValues();

				assertAll(
					() -> assertThat(values).containsKey(TO_APPLY),
					() -> assertThat(values).containsKey(DOCUMENT)
				);
			}
		}

		@DisplayName("1개의 지원 예정 혹은 서류 전형을 받을 경우")
		@Nested
		class Context_with_Only_Process_Of_To_Apply_Or_Document {

			@DisplayName("동일한 일자의 지원 예정, 서류 전형을 가진 Processes를 반환한다.")
			@ParameterizedTest
			@EnumSource(value = ProcessType.class, names = {"TO_APPLY", "DOCUMENT"})
			void it_returns_processes_with_to_apply_and_document(ProcessType processType) {
				Process process = new Process(null, processType, "세부직무", LocalDateTime.now());

				Processes actual = Processes.initialFrom(process);

				Map<ProcessType, List<Process>> processes = actual.getValues();
				Process firstProcess = processes.get(TO_APPLY).get(0);
				Process secondProcess = processes.get(DOCUMENT).get(0);
				List<ProcessType> processesTypes = List.of(firstProcess.getType(), secondProcess.getType());
				assertAll(
					() -> assertThat(processes).hasSize(2),
					() -> assertThat(firstProcess.getSchedule()).isEqualTo(secondProcess.getSchedule()),
					() -> assertThat(processesTypes).containsExactly(TO_APPLY, DOCUMENT)
				);
			}
		}

		@DisplayName("지원 예정 혹은 서류가 아닌 1개의 전형을 받을 경우")
		@Nested
		class Context_with_Only_Process_That_Is_Not_To_Apply_Or_Document {

			@DisplayName("해당 전형만 가진 Processes를 반환한다.")
			@ParameterizedTest
			@EnumSource(value = ProcessType.class, names = {"TEST", "INTERVIEW", "COMPLETE"})
			void it_returns_processes_with_only_that_process(ProcessType processType) {
				Process process = new Process(null, processType, "세부직무", LocalDateTime.now());

				Processes actual = Processes.initialFrom(process);

				Map<ProcessType, List<Process>> processes = actual.getValues();
				assertAll(
					() -> assertThat(processes.keySet()).hasSize(1),
					() -> assertThat(processes.get(processType)).contains(process)
				);
			}
		}
	}

	@DisplayName("getSortedValues 메소드는")
	@Nested
	class Describe_getSortedValues {

		@DisplayName("여러 전형을 갖고 있을 경우")
		@Nested
		class Context_with_many_processes {

			private final Process toApply = ProcessFixture.TO_APPLY.toDomainWithoutOrder();
			private final Process document = ProcessFixture.DOCUMENT.toDomainWithoutOrder();
			private final Process test1 = TEST.toDomainWithoutOrder();
			private final Process test2 = CODING_TEST.toDomainWithoutOrder();
			private final Process interview1 = FIRST_INTERVIEW.toDomainWithoutOrder();
			private final Process interview2 = SECOND_INTERVIEW.toDomainWithoutOrder();
			private final List<Process> input = List.of(toApply, document, test1, test2, interview1, interview2);

			@DisplayName("ProccessType과 Order에 따라 정렬된 전형들을 반환한다.")
			@Test
			void it_returns_sorted_values() {
				Processes processes = Processes.initialFrom(input);
				List<Process> sortedProcesses = processes.getSortedValues();

				assertThat(sortedProcesses).containsOnly(toApply, document, test1, test2, interview1, interview2);
			}
		}
	}
}
