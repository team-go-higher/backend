package gohigher.application.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProcessResponse {

	private final Long id;
	private final String type;
	private final String description;
	private final String schedule;
}
