package gohigher.application.port.in;

import java.util.List;

import gohigher.application.Application;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpecificApplicationRequest {

	private String companyName;
	private String team;
	private String location;
	private String contact;
	private String duty;
	private String position;
	private String jobDescription;
	private String workType;
	private String employmentType;
	private String careerRequirement;
	private String requiredCapability;
	private String preferredQualification;
	private List<SpecificApplicationProcessRequest> processes;
	private String url;

	public Application toDomain(Long userId) {
		List<Process> processes = this.processes.stream()
			.map(SpecificApplicationProcessRequest::toDomain)
			.toList();
		return new Application(companyName, team, location, contact, duty, position, jobDescription, workType,
			EmploymentType.from(employmentType), careerRequirement, requiredCapability, preferredQualification, null,
			processes, url, userId, processes.get(0));
	}
}
