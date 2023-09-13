package gohigher.application;

import java.time.LocalDateTime;
import java.util.List;

import gohigher.common.EmploymentType;
import gohigher.common.JobInfo;
import gohigher.common.Process;
import lombok.Getter;

/**
 * currentProcess   현재 프로세스
 */
@Getter
public class Application extends JobInfo {

	private final Process currentProcess;

	public Application(Long id, String companyName, String team, String location, String contact, String duty,
		String position, String jobDescription, String workType, EmploymentType employmentType,
		String careerRequirement, String requiredCapability, String preferredQualification, LocalDateTime deadLine,
		List<Process> processes, String url, Process currentProcess) {
		super(id, companyName, team, location, contact, duty, position, jobDescription, workType, employmentType,
			careerRequirement, requiredCapability, preferredQualification, deadLine, processes, url);
		this.currentProcess = currentProcess;
	}

	public static Application simple(String companyName, String duty, String url, Process process) {
		return new Application(null, companyName, null, null, null, duty, null, null,
			null, null, null, null, null, null,
			List.of(process), url, process);
	}
}
