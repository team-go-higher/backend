package gohigher.application.port.in;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SpecificApplicationUpdateRequest {

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
	private List<SpecificApplicationProcessUpdateRequest> processes;
	private String url;
}
