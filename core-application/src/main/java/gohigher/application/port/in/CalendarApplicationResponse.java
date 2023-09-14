package gohigher.application.port.in;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CalendarApplicationResponse {

	private final Long applicationId;
	private final Long processId;
	private final String name;
	private final String processType;
	private final LocalDateTime schedule;
}
