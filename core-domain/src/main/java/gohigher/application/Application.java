package gohigher.application;

import java.time.LocalDateTime;

import gohigher.common.EmploymentType;
import gohigher.common.JobInfo;
import gohigher.common.Process;
import gohigher.common.Processes;
import lombok.Getter;

/**
 * currentProcess   현재 프로세스
 */
@Getter
public class Application extends JobInfo {

	private final Process currentProcess;

	public Application(Long id, String companyName, String team, String location, String contact, String position,
		String specificPosition, String jobDescription, String workType, EmploymentType employmentType,
		String careerRequirement, String requiredCapability, String preferredQualification, Processes processes,
		String url, Process currentProcess) {
		super(id, companyName, team, location, contact, position, specificPosition, jobDescription, workType,
			employmentType, careerRequirement, requiredCapability, preferredQualification, processes, url);
		this.currentProcess = currentProcess;
	}

	public static Application simple(String companyName, String position, String url, Process process) {
		return new Application(null, companyName, null, null, null, position, null, null,
			null, null, null, null, null, Processes.initialFrom(process), url, process);
	}

	public void updateSimply(String companyName, String position, String url, Long processId, LocalDateTime schedule) {
		Process process = processes.getValueById(processId);
		process.updateSchedule(schedule);
		this.companyName = companyName;
		this.position = position;
		this.url = url;
	}
}
