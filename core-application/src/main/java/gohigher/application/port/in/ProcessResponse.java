package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.common.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProcessResponse {

	private final Long id;
	private final String type;
	private final String description;
	private final LocalDateTime schedule;

	public static ProcessResponse from(Process process) {
		return new ProcessResponse(process.getId(), process.getType().name(), process.getDescription(),
			process.getSchedule());
	}
}
