package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.common.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SpecificProcessResponse {

	private final Long id;
	private final String type;
	private final String description;
	private final LocalDateTime schedule;
	private final Boolean isCurrent;

	public static SpecificProcessResponse of(Process process, Process currentProcess) {
		return new SpecificProcessResponse(process.getId(), process.getType().name(), process.getDescription(),
			process.getSchedule(), process.equals(currentProcess));
	}
}
