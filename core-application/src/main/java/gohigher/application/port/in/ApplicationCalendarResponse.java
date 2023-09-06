package gohigher.application.port.in;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplicationCalendarResponse {

	private final String name;
	private final String processType;
	private final LocalDateTime schedule;
}
