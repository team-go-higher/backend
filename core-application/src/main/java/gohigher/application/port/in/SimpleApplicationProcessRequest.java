package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.common.Process;
import gohigher.common.ProcessType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleApplicationProcessRequest {

	private String type;

	private String description;

	private LocalDateTime schedule;

	public ProcessType getType() {
		return ProcessType.from(type);
	}

	public Process toDomain() {
		return new Process(null, ProcessType.from(type), description, schedule);
	}
}
