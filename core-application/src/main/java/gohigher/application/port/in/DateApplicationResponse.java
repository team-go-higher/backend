package gohigher.application.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DateApplicationResponse {

	private final Long applicationId;
	private final ProcessResponse process;
}
