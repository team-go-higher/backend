package gohigher.application.port.in;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleApplicationUpdateRequest {

	@NotBlank(message = "JOB_INFO_002||회사명이 입력되지 않았습니다.")
	private String companyName;

	@NotBlank(message = "JOB_INFO_003||직무가 입력되지 않았습니다.")
	private String position;

	@NotNull(message = "APPLICATION_004||지원서의 전형 id가 입력되지 않았습니다.")
	private Long processId;

	private LocalDateTime schedule;

	private String url;
}
