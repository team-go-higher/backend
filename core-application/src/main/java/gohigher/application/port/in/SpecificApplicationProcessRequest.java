package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.common.Process;
import gohigher.common.ProcessType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SpecificApplicationProcessRequest {

	@NotBlank(message = "JOB_INFO_005||전형 단계가 입력되지 않았습니다.")
	private String type;
	@NotBlank(message = "JOB_INFO_006||세부 전형이 입력되지 않았습니다.")
	private String description;

	private LocalDateTime schedule;
	private Boolean isCurrent;

	public SpecificApplicationProcessRequest(String type, String description, LocalDateTime schedule,
		Boolean isCurrent) {
		this.type = type;
		this.description = description;
		this.schedule = schedule;
		this.isCurrent = isCurrent;
	}

	public Process toDomain() {
		return new Process(ProcessType.from(type), description, schedule);
	}
}
