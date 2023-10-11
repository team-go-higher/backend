package gohigher.common;

import static gohigher.application.ProcessFixture.*;
import static gohigher.common.ProcessType.DOCUMENT;
import static gohigher.common.ProcessType.TO_APPLY;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import gohigher.application.ProcessFixture;

@DisplayName("Processes 클래스의")
class ProcessesTest {

	@DisplayName("initiallyFrom 정적 팩터리 메소드는")
	@Nested
	class Describe_initiallyFrom {

		@DisplayName("Process들을 받을 경우")
		@Nested
		class Context_with_processes_that_not_assigned_order {

			private final Process toApply = ProcessFixture.TO_APPLY.toDomainWithoutOrder();
			private final Process document = ProcessFixture.DOCUMENT.toDomainWithoutOrder();
			private final Process test = TEST.toDomainWithoutOrder();
			private final Process interview = INTERVIEW.toDomainWithoutOrder();
			private final List<Process> input = List.of(toApply, document, test, interview);

			@DisplayName("Order를 순차적으로 할당하며 생성한다.")
			@Test
			void it_returns_processes_that_assigned_order() {
				Processes processes = Processes.initialFrom(input);

				assertAll(
					() -> assertThat(processes.getValues().get(0).getOrder()).isEqualTo(1),
					() -> assertThat(processes.getValues().get(1).getOrder()).isEqualTo(2),
					() -> assertThat(processes.getValues().get(2).getOrder()).isEqualTo(3),
					() -> assertThat(processes.getValues().get(3).getOrder()).isEqualTo(4)
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
				Processes processes = Processes.initialFrom(input);

				assertAll(
					() -> assertThat(processes.getValues()).hasSize(1),
					() -> assertThat(processes.getValues().get(0).getType()).isEqualTo(ProcessType.TO_APPLY)
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

				List<Process> processes = actual.getValues();
				Process firstProcess = processes.get(0);
				Process secondProcess = processes.get(1);
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

				List<Process> processes = actual.getValues();
				assertAll(
					() -> assertThat(processes).hasSize(1),
					() -> assertThat(processes.get(0).getType()).isEqualTo(processType)
				);
			}
		}
	}
}