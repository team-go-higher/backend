package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.common.Process;
import gohigher.common.ProcessType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleApplicationProcessRequest {

	@NotBlank(message = "JOB_INFO_005||전형 단계가 입력되지 않았습니다.")
	private String type;

	@NotBlank(message = "JOB_INFO_006||세부 전형이 입력되지 않았습니다.")
	private String description;

	private LocalDateTime schedule;

	public Process toDomain() {
		return new Process(null, ProcessType.from(type), description, schedule);
	}
}
