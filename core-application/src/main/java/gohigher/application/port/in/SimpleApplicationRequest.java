package gohigher.application.port.in;

import gohigher.application.Application;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleApplicationRequest {

	@NotBlank(message = "JOB_INFO_002||회사명이 입력되지 않았습니다.")
	private String companyName;

	@NotBlank(message = "JOB_INFO_003||직무가 입력되지 않았습니다.")
	private String duty;

	private String url;

	private SimpleApplicationProcessRequest currentProcess;

	public Application toDomain() {
		return Application.simple(companyName, duty, url, currentProcess.getType(), currentProcess.getSchedule());
	}
}
