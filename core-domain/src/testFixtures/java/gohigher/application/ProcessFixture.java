package gohigher.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import gohigher.common.Process;
import gohigher.common.ProcessType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcessFixture {

	TO_APPLY(ProcessType.TO_APPLY, "지원 예정", LocalDateTime.now().plusDays(6), 1),
	DOCUMENT(ProcessType.DOCUMENT, "서류 전형", LocalDateTime.now().plusDays(6), 2),
	TEST(ProcessType.TEST, "시험", LocalDateTime.now().plusDays(10), 3),
	CODING_TEST(ProcessType.TEST, "코딩테스트", LocalDateTime.now().plusDays(10), 4),
	INTERVIEW(ProcessType.INTERVIEW, "면접 전형", LocalDateTime.now().plusDays(20), 5),
	FIRST_INTERVIEW(ProcessType.INTERVIEW, "1차 기술 면접", LocalDateTime.now().plusDays(20), 6),
	SECOND_INTERVIEW(ProcessType.INTERVIEW, "2차 통합 면접", LocalDateTime.now().plusDays(20), 7),
	COMPLETE(ProcessType.COMPLETE, "종료", LocalDateTime.now().plusDays(40), 8),
	;

	private final ProcessType type;
	private final String description;
	private final LocalDateTime schedule;
	private final int order;

	public Process toDomain() {
		return createProcess(null, type, description, schedule, order);
	}

	public Process toDomainWithSchedule(LocalDateTime schedule) {
		return createProcess(null, type, description, schedule, order);
	}

	public Process toDomainWithSchedule(LocalDate date) {
		return createProcess(null, type, description, LocalDateTime.of(date, LocalTime.now()), order);
	}

	public Process toDomainWithoutOrder() {
		return createProcess(null, type, description, schedule, 0);
	}

	public Process toPersistedDomain(long id) {
		return createProcess(id, type, description, schedule, order);
	}

	private Process createProcess(Long id, ProcessType type, String description, LocalDateTime schedule, int order) {
		return new Process(id, type, description, schedule, order);
	}
}
