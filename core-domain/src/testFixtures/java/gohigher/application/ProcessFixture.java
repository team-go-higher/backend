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

	TO_APPLY(ProcessType.TO_APPLY, "", null),
	DOCUMENT(ProcessType.DOCUMENT, "", LocalDateTime.now().plusDays(6)),
	TEST(ProcessType.TEST, "", LocalDateTime.now().plusDays(10)),
	INTERVIEW(ProcessType.INTERVIEW, "", LocalDateTime.now().plusDays(20)),
	COMPLETE(ProcessType.COMPLETE, "", LocalDateTime.now().plusDays(40)),
	;

	private final ProcessType type;
	private final String description;
	private final LocalDateTime schedule;

	public Process toDomain() {
		return new Process(null, this.getType(), this.getDescription(), this.getSchedule());
	}

	public Process toDomainWithSchedule(LocalDateTime schedule) {
		return new Process(null, this.getType(), this.getDescription(), schedule);
	}

	public Process toDomainWithSchedule(LocalDate date) {
		return toDomainWithSchedule(LocalDateTime.of(date, LocalTime.now()));
	}

	public Process toDomainWithDescription(String description) {
		return new Process(null, this.getType(), description, this.getSchedule());
	}
}
