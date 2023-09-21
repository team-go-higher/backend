package gohigher.application.port.in;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import gohigher.common.Process;
import gohigher.common.ProcessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleApplicationProcessRequest {

	@NotNull(message = "JOB_INFO_005||전형 단계가 입력되지 않았습니다.")
	@NotBlank(message = "JOB_INFO_005||전형 단계가 입력되지 않았습니다.")
	private String type;

	@NotNull(message = "JOB_INFO_006||세부 전형이 입력되지 않았습니다.")
	@NotBlank(message = "JOB_INFO_006||세부 전형이 입력되지 않았습니다.")
	private String description;

	@NotNull(message = "JOB_INFO_007||전형 일정이 입력되지 않았습니다.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime schedule;

	public ProcessType getType() {
		return ProcessType.from(type);
	}

	public Process toDomain() {
		return new Process(null, ProcessType.from(type), description, schedule);
	}
}
