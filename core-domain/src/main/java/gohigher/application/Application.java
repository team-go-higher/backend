package gohigher.application;

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

	private final Long userId;
	private final Process currentProcess;

	public Application(Long id, String companyName, String team, String location, String contact, String duty,
		String position, String jobDescription, String workType, EmploymentType employmentType,
		String careerRequirement, String requiredCapability, String preferredQualification, List<Process> processes,
		String url, Long userId, Process currentProcess) {
		super(id, companyName, team, location, contact, duty, position, jobDescription, workType, employmentType,
			careerRequirement, requiredCapability, preferredQualification, processes, url);
		this.userId = userId;
		this.currentProcess = currentProcess;
	}

	public static Application simple(Long userId, String companyName, String duty, String url, Process process) {
		return new Application(null, companyName, null, null, null, duty, null, null,
			null, null, null, null, null, List.of(process), url, userId, process);
	}

	public boolean isAppliedBy(Long userId) {
		return this.userId.equals(userId);
	}
}
