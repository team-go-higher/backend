package gohigher.common;

import static gohigher.application.ApplicationErrorCode.*;
import static gohigher.application.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gohigher.application.ApplicationErrorCode;
import gohigher.application.ProcessFixture;
import gohigher.global.exception.GoHigherException;

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

		@DisplayName("비어있는 전형 목록을 받을 경우")
		@Nested
		class Context_with_empty_processes {

			private final List<Process> processes = Collections.emptyList();

			@DisplayName("예외를 발생시킨다.")
			@Test
			void it_throws_exception() {
				assertThatThrownBy(() -> Processes.initialFrom(processes))
					.hasMessage(APPLICATION_PROCESS_NOT_EXIST.getMessage());
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

	@DisplayName("updateSchedule 메소드는")
	@Nested
	class Describe_updateSchedule {

		private final Long targetId = 1L;
		private final LocalDateTime scheduleToUpdate = LocalDateTime.now().plusDays(10);

		@DisplayName("조회하려는 id를 가진 Process 객체를 갖고 있을 경우")
		@Nested
		class Context_with_target_process {

			private final Long otherId = 2L;
			private final Process test = TEST.toPersistedDomain(targetId);
			private final Process interview = INTERVIEW.toPersistedDomain(otherId);
			Processes processes = Processes.initialFrom(List.of(test, interview));

			@DisplayName("해당 객체를 반환한다")
			@Test
			void it_returns_process() {
				processes.updateSchedule(targetId, scheduleToUpdate);

				LocalDateTime actual = processes.getValueById(targetId).getSchedule();
				assertThat(actual).isEqualTo(scheduleToUpdate);
			}
		}
	}

	@DisplayName("getValueById 메소드는")
	@Nested
	class Describe_getValueById {

		private final Long targetId = 1L;

		@DisplayName("조회하려는 id를 가진 Process 객체를 갖고 있을 경우")
		@Nested
		class Context_with_target_process {

			private final Process expected = TEST.toPersistedDomain(targetId);
			private final Processes processes = Processes.initialFrom(List.of(expected));

			@DisplayName("해당 객체를 반환한다")
			@Test
			void it_returns_process() {
				Process actual = processes.getValueById(targetId);

				assertThat(actual).isEqualTo(expected);
			}
		}

		@DisplayName("조회하려는 id를 가진 Process 객체를 갖고 있지 않을 경우")
		@Nested
		class Context_without_target_process {

			private final Long otherId = 2L;
			private final Process testProcess = TEST.toPersistedDomain(otherId);
			private final Processes processes = Processes.initialFrom(List.of(testProcess));

			@DisplayName("예외를 발생시킨다.")
			@Test
			void it_throws_exception() {
				assertThatThrownBy(() -> processes.getValueById(targetId))
					.isInstanceOf(GoHigherException.class)
					.hasMessage(ApplicationErrorCode.APPLICATION_PROCESS_NOT_FOUND.getMessage());
			}
		}
	}
}
