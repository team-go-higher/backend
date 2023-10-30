package gohigher.common;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * order는 설정되어있지 않으면 기본 값이 0, 실제 Order는 1부터 시작
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Process {

	private final Long id;
	private final ProcessType type;
	private final String description;
	private LocalDateTime schedule;

	private int order;

	public Process(Long id, ProcessType type, String description, LocalDateTime schedule) {
		this(id, type, description, schedule, 0);
	}

	public Process(ProcessType type, String description, LocalDateTime schedule) {
		this(null, type, description, schedule);
	}

	public void assignOrder(int order) {
		this.order = order;
	}

	public void updateSchedule(LocalDateTime schedule) {
		this.schedule = schedule;
	}
}
