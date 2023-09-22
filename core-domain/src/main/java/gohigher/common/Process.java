package gohigher.common;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Process {

	private final Long id;
	private final ProcessType type;
	private final String description;
	private final LocalDateTime schedule;

	private int order;

	public Process(ProcessType type, String description, LocalDateTime schedule) {
		this(null, type, description, schedule);
	}

	public void assignOrder(int order) {
		this.order = order;
	}
}
