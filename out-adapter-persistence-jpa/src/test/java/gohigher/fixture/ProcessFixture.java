package gohigher.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcessFixture {

	TO_APPLY(ProcessType.TO_APPLY, "", LocalDateTime.now()),
	DOCUMENT(ProcessType.DOCUMENT, "", LocalDateTime.now()),
	TEST(ProcessType.TEST, "", LocalDateTime.now()),
	INTERVIEW(ProcessType.INTERVIEW, "", LocalDateTime.now()),
	COMPLETE(ProcessType.COMPLETE, "", LocalDateTime.now()),
	;

	private final ProcessType type;
	private final String description;
	private final LocalDateTime schedule;

	public Process toDomainWithSchedule(LocalDateTime schedule) {
		return new Process(this.getType(), this.getDescription(), schedule);
	}

	public ApplicationProcessJpaEntity toApplicationProcessEntity(
		ApplicationJpaEntity applicationJpaEntity, LocalDate date) {
		return ApplicationProcessJpaEntity.of(applicationJpaEntity,
			this.toDomainWithSchedule(LocalDateTime.of(date, LocalTime.now())), 0);
	}

	public ApplicationProcessJpaEntity toApplicationProcessEntity(ApplicationJpaEntity applicationJpaEntity,
		LocalDateTime schedule, int order) {
		return ApplicationProcessJpaEntity.of(applicationJpaEntity, this.toDomainWithSchedule(schedule), order);
	}
}
