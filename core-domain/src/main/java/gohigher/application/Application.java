package gohigher.application;

import java.time.LocalDateTime;
import java.util.List;

import gohigher.common.EmploymentType;
import gohigher.common.JobInfo;
import gohigher.common.Process;

/**
 * currentProcess   현재 프로세스
 */
public class Application extends JobInfo {

	private final int currentProcess;

	public Application(String companyName, String location, String contact, String duty, String jobDescription,
		String workType, EmploymentType employmentType, String careerRequirement, String requiredCapability,
		String preferredQualification, LocalDateTime deadLine, List<Process> processes,
		String url, int currentProcess) {
		super(companyName, location, contact, duty, jobDescription, workType, employmentType, careerRequirement,
			requiredCapability, preferredQualification, deadLine, processes, url);
		this.currentProcess = currentProcess;
	}
}
