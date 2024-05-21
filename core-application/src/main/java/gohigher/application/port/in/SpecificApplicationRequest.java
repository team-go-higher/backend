package gohigher.application.port.in;

import java.util.List;

import gohigher.application.Application;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import gohigher.common.Processes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpecificApplicationRequest {

	@NotBlank(message = "JOB_INFO_002||회사명이 입력되지 않았습니다.")
	private String companyName;
	private String team;
	private String location;
	private String contact;

	@NotBlank(message = "JOB_INFO_003||직무가 입력되지 않았습니다.")
	private String position;
	private String specificPosition;
	private String jobDescription;
	private String workType;
	private String employmentType;
	private String careerRequirement;
	private String requiredCapability;
	private String preferredQualification;

	@Valid
	private List<SpecificApplicationProcessRequest> processes;
	private String url;

	public Application toDomain() {
		List<Process> processes = this.processes.stream()
			.map(SpecificApplicationProcessRequest::toDomain)
			.toList();

		return Application.specify(null, companyName, team, location, contact, position, specificPosition,
			jobDescription,
			workType, EmploymentType.from(employmentType), careerRequirement, requiredCapability,
			preferredQualification, Processes.initialFrom(processes), url, processes.get(0));
	}
}
