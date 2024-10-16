package gohigher.application.port.in;

import java.util.ArrayList;
import java.util.List;

import gohigher.application.Application;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import gohigher.common.Processes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
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

	public SpecificApplicationRequest(String companyName, String team, String location, String contact,
		String position, String specificPosition, String jobDescription, String workType, String employmentType,
		String careerRequirement, String requiredCapability, String preferredQualification,
		List<SpecificApplicationProcessRequest> processes, String url) {
		this.companyName = companyName;
		this.team = team;
		this.location = location;
		this.contact = contact;
		this.position = position;
		this.specificPosition = specificPosition;
		this.jobDescription = jobDescription;
		this.workType = workType;
		this.employmentType = employmentType;
		this.careerRequirement = careerRequirement;
		this.requiredCapability = requiredCapability;
		this.preferredQualification = preferredQualification;
		this.processes = processes;
		this.url = url;
	}

	public Application toDomain() {
		List<Process> processDomains = new ArrayList<>();
		int currentProcessIdx = 0;

		for (int i = 0; i < processes.size(); i++) {
			if (processes.get(i).getIsCurrent()) {
				currentProcessIdx = i;
			}
			processDomains.add(processes.get(i).toDomain());
		}

		return Application.specify(null, companyName, team, location, contact, position, specificPosition,
			jobDescription,
			workType, EmploymentType.from(employmentType), careerRequirement, requiredCapability,
			preferredQualification, Processes.initialFrom(processDomains), url, processDomains.get(currentProcessIdx));
	}
}
