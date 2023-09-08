package gohigher.common;

import java.time.LocalDateTime;

public record Process(
	ProcessType type,
	String description,
	LocalDateTime schedule
) {

	public boolean hasType(ProcessType type) {
		return this.type == type;
	}
}
