package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.common.Process;
import gohigher.common.ProcessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpecificApplicationProcessRequest {

	@NotBlank(message = "JOB_INFO_005||전형 단계가 입력되지 않았습니다.")
	private String type;
	@NotBlank(message = "JOB_INFO_006||세부 전형이 입력되지 않았습니다.")
	private String description;
	@NotNull(message = "JOB_INFO_007||전형 일정이 입력되지 않았습니다.")
	private LocalDateTime schedule;

	public Process toDomain() {
		return new Process(ProcessType.from(type), description, schedule);
	}
}
