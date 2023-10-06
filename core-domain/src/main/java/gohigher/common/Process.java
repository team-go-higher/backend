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
	private final LocalDateTime schedule;

	private int order;

	public Process(ProcessType type, String description, LocalDateTime schedule, int order) {
		this(null, type, description, schedule, order);
	}

	public Process(ProcessType type, String description, LocalDateTime schedule) {
		this(null, type, description, schedule);
	}

	public void assignOrder(int order) {
		this.order = order;
	}
}
