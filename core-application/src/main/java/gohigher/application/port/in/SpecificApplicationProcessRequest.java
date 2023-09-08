package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.common.Process;
import gohigher.common.ProcessType;

public class SpecificApplicationProcessRequest {

	private String type;
	private String description;
	private LocalDateTime schedule;

	public Process toDomain() {
		return new Process(ProcessType.from(type), description, schedule);
	}
}
