package gohigher.application.port.in;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplicationProcessResponse {

	private final Long id;
	private final String type;
	private final String description;
	private final LocalDateTime schedule;
}
