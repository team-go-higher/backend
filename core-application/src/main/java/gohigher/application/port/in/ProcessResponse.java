package gohigher.application.port.in;

import java.time.format.DateTimeFormatter;

import gohigher.common.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProcessResponse {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME.getFormat());

	private final Long id;
	private final String type;
	private final String description;
	private final String schedule;

	public static ProcessResponse from(Process process) {
		return new ProcessResponse(process.getId(), process.getType().name(), process.getDescription(),
			process.getSchedule().format(FORMATTER));
	}
}
