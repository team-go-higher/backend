package gohigher.application.port.in;

import gohigher.common.Process;
import gohigher.common.ProcessType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnscheduledProcessRequest {

	@NotBlank(message = "JOB_INFO_005||전형 단계가 입력되지 않았습니다.")
	private String type;

	@NotBlank(message = "JOB_INFO_006||세부 전형이 입력되지 않았습니다.")
	private String description;

	public Process toDomain() {
		return new Process(ProcessType.from(type), description, null);
	}
}
