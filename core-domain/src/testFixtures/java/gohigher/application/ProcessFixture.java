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

	TO_APPLY(ProcessType.TO_APPLY, "지원 예정", null, 0),
	DOCUMENT(ProcessType.DOCUMENT, "서류 전형", LocalDateTime.now().plusDays(6), 1),
	TEST(ProcessType.TEST, "시험", LocalDateTime.now().plusDays(10), 2),
	CODING_TEST(ProcessType.TEST, "코딩테스트", LocalDateTime.now().plusDays(10), 3),
	INTERVIEW(ProcessType.INTERVIEW, "면접 전형", LocalDateTime.now().plusDays(20), 4),
	FIRST_INTERVIEW(ProcessType.INTERVIEW, "1차 기술 면접", LocalDateTime.now().plusDays(20), 5),
	SECOND_INTERVIEW(ProcessType.INTERVIEW, "2차 통합 면접", LocalDateTime.now().plusDays(20), 6),
	COMPLETE(ProcessType.COMPLETE, "종료", LocalDateTime.now().plusDays(40), 7),
	;

	private final ProcessType type;
	private final String description;
	private final LocalDateTime schedule;
	private final int order;

	public Process toDomain() {
		return new Process(null, this.getType(), this.getDescription(), this.getSchedule(), this.order);
	}

	public Process toDomainWithoutOrder() {
		return new Process(null, this.getType(), this.getDescription(), this.getSchedule(), null);
	}

	public Process toDomainWithSchedule(LocalDateTime schedule) {
		return new Process(null, this.getType(), this.getDescription(), schedule, order);
	}

	public Process toDomainWithSchedule(LocalDate date) {
		return toDomainWithSchedule(LocalDateTime.of(date, LocalTime.now()));
	}
}
