package gohigher.application.port.in;

import java.time.LocalDateTime;
import java.util.List;

import gohigher.common.EmploymentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplicationResponse {

	private final Long id;
	private final String companyName;
	private final String team;
	private final String location;
	private final String contact;
	private final String duty;
	private final String position;
	private final String jobDescription;
	private final String workType;
	private final EmploymentType employmentType;
	private final String careerRequirement;
	private final String requiredCapability;
	private final String preferredQualification;
	private final LocalDateTime deadline;
	private final List<ProcessResponse> processes;
	private final int currentProcessOrder;
	private final String url;
}
