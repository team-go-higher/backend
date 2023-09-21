package gohigher.application.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmptyScheduleApplicationResponse {

	private final long id;
	private final String companyName;
	private final String position;
	private final String specificPosition;
	private final String processType;
	private final String processDescription;
}
