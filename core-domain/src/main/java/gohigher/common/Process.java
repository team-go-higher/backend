package gohigher.common;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Process {

	private final Long id;
	private final Integer order;
	private final ProcessType type;
	private final String description;
	private final LocalDateTime schedule;

	public Process(Integer order, ProcessType type, String description, LocalDateTime schedule) {
		this(null, order, type, description, schedule);
	}

	public Process(ProcessType type, String description, LocalDateTime schedule) {
		this(null, type, description, schedule);
	}
}
