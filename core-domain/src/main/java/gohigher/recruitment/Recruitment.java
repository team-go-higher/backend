package gohigher.recruitment;

import java.time.LocalDateTime;
import java.util.List;

import gohigher.common.EmploymentType;
import gohigher.common.JobInfo;
import gohigher.common.Process;

public class Recruitment extends JobInfo {

	public Recruitment(String companyName, String location, String contact, String duty, String jobDescription,
		String workType, EmploymentType employmentType, String careerRequirement, String requiredCapability,
		String preferredQualification, LocalDateTime deadLine, List<Process> processes, String url) {
		super(companyName, location, contact, duty, jobDescription, workType, employmentType, careerRequirement,
			requiredCapability, preferredQualification, deadLine, processes, url);
	}
}
