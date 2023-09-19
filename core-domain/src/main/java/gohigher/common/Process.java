package gohigher.common;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Process {

	private final Long id;
	private final ProcessType type;
	private final String description;
	private final LocalDateTime schedule;

	public Process(ProcessType type, String description, LocalDateTime schedule) {
		this(null, type, description, schedule);
	}

	public boolean hasType(ProcessType type) {
		return this.type == type;
	}

	public static Process toApply() {
		return new Process(null, ProcessType.TO_APPLY, null, null);
	}
}
