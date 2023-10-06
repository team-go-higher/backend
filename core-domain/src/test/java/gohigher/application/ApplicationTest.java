package gohigher.application;

import static gohigher.common.ProcessType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import gohigher.common.Process;
import gohigher.common.ProcessType;

@DisplayName("Application 클래스의")
class ApplicationTest {

	@DisplayName("simple 메서드는")
	@Nested
	class Describe_Simple {

		@DisplayName("지원 예정 혹은 서류 전형의 지원서를 생성하려고 할 때,")
		@Nested
		class Context_Construction_With_ToApply_Or_Document_Process {

			@DisplayName("동일한 전형일의 지원 예정과 서류 전형를 순서대로 가지는 지원서를 반환한다.")
			@ParameterizedTest
			@EnumSource(value = ProcessType.class, names = {"TO_APPLY", "DOCUMENT"})
			void it_Returns_Application_With_ToApply_And_Document(ProcessType processType) {
				Process process = new Process(null, processType, "세부직무", LocalDateTime.now());

				Application application = Application.simple("회사명", "포지션", "url", process);

				List<Process> processes = application.getProcesses();
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

		@DisplayName("지원 예정 혹은 서류가 아닌 전형의 지원서를 생성하려고 할 때,")
		@Nested
		class Context_Construction_Without_ToApply_Or_Document_Process {

			@DisplayName("해당 전형만 가지는 지원서를 응답한다.")
			@ParameterizedTest
			@EnumSource(value = ProcessType.class, names = {"TEST", "INTERVIEW", "COMPLETE"})
			void it_Returns_Application_With_Only_ProcessType(ProcessType processType) {
				Process process = new Process(null, processType, "세부직무", LocalDateTime.now());

				Application application = Application.simple("회사명", "포지션", "url", process);

				List<Process> processes = application.getProcesses();
				assertAll(
					() -> assertThat(processes).hasSize(1),
					() -> assertThat(processes.get(0).getType()).isEqualTo(processType)
				);
			}
		}
	}
}
