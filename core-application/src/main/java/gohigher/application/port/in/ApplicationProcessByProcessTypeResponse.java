package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.common.Process;
import gohigher.common.ProcessType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplicationProcessByProcessTypeResponse {

	private final long id;
	private final ProcessType type;
	private final LocalDateTime schedule;

	public static ApplicationProcessByProcessTypeResponse from(Process process) {
		return new ApplicationProcessByProcessTypeResponse(process.getId(), process.getType(), process.getSchedule());
	}
}
