package gohigher.common;

import java.time.LocalDateTime;

public record Process(
	ProcessType type,
	String description,
	LocalDateTime schedule
) {
}
