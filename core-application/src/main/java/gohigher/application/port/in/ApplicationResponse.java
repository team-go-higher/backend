package gohigher.application.port.in;

import java.util.List;

import gohigher.application.Application;
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
	private final String position;
	private final String specificPosition;
	private final String jobDescription;
	private final String workType;
	private final String employmentType;
	private final String careerRequirement;
	private final String requiredCapability;
	private final String preferredQualification;
	private final List<SpecificProcessResponse> processes;
	private final String url;

	public static ApplicationResponse from(Application application) {
		List<SpecificProcessResponse> processResponses = application.getProcesses()
			.stream()
			.map(process -> SpecificProcessResponse.of(process, application.getCurrentProcess()))
			.toList();

		return new ApplicationResponse(
			application.getId(),
			application.getCompanyName(),
			application.getTeam(),
			application.getLocation(),
			application.getContact(),
			application.getPosition(),
			application.getSpecificPosition(),
			application.getJobDescription(),
			application.getWorkType(),
			getEmploymentType(application),
			application.getCareerRequirement(),
			application.getRequiredCapability(),
			application.getPreferredQualification(),
			processResponses,
			application.getUrl()
		);
	}

	private static String getEmploymentType(Application application) {
		EmploymentType employmentType = application.getEmploymentType();
		return employmentType == null ? null : employmentType.getValue();
	}
}
