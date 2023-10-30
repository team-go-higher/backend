package gohigher.application.port.in;

import gohigher.common.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplicationProcessByProcessTypeResponse {

	private final long id;
	private final String description;

	public static ApplicationProcessByProcessTypeResponse from(Process process) {
		return new ApplicationProcessByProcessTypeResponse(process.getId(), process.getDescription());
	}
}
