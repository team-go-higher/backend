package gohigher.application.port.in;

import gohigher.application.Application;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleApplicationCommand {

	@NotBlank(message = "companyName is blank")
	private String companyName;
	@NotBlank(message = "duty is blank")
	private String duty;

	private SimpleProcessCommand currentProcess;

	public Application toDomain() {
		return Application.simple(companyName, duty, currentProcess.getType(), currentProcess.getSchedule());
	}
}