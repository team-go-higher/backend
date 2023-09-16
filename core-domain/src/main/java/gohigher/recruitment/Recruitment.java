package gohigher.recruitment;

import java.util.List;

import gohigher.common.EmploymentType;
import gohigher.common.JobInfo;
import gohigher.common.Process;

public class Recruitment extends JobInfo {

	public Recruitment(Long id, String companyName, String team, String location, String contact, String duty,
		String position, String jobDescription, String workType, EmploymentType employmentType,
		String careerRequirement, String requiredCapability, String preferredQualification, List<Process> processes,
		String url) {
		super(id, companyName, team, location, contact, duty, position, jobDescription, workType, employmentType,
			careerRequirement, requiredCapability, preferredQualification, processes, url);
	}
}
