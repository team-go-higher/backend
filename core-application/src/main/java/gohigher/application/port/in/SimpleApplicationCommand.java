package gohigher.application.port.in;

import gohigher.application.Application;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleApplicationCommand {

	@NotBlank(message = "회사명이 빈 값입니다.")
	private String companyName;

	@NotBlank(message = "직무가 빈 값입니다.")
	private String duty;

	private SimpleProcessCommand currentProcess;

	public Application toDomain() {
		return Application.simple(companyName, duty, currentProcess.getType(), currentProcess.getSchedule());
	}
}
