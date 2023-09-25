package gohigher.application.port.in;

import java.util.List;

import gohigher.application.Application;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import gohigher.common.Processes;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpecificApplicationUpdateRequest {

	@NotBlank(message = "JOB_INFO_008||지원서 정보가 입력되지 않았습니다.")
	private Long applicationId;
	private String companyName;
	private String team;
	private String location;
	private String contact;
	private String position;
	private String specificPosition;
	private String jobDescription;
	private String workType;
	private String employmentType;
	private String careerRequirement;
	private String requiredCapability;
	private String preferredQualification;
	private List<SpecificApplicationUpdateProcessRequest> processes;
	private String url;
	private int currentProcessOrder;

	public Application toDomain() {
		List<Process> processes = this.processes.stream()
			.map(SpecificApplicationUpdateProcessRequest::toDomain)
			.toList();

		return new Application(applicationId, companyName, team, location, contact, position, specificPosition,
			jobDescription, workType, EmploymentType.from(employmentType), careerRequirement, requiredCapability,
			preferredQualification, Processes.initiallyFrom(processes), url, processes.get(currentProcessOrder));
	}
}
