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
	private final CompletedSwitch completedSwitch;

	public Application(Long id, String companyName, String team, String location, String contact, String position,
		String specificPosition, String jobDescription, String workType, EmploymentType employmentType,
		String careerRequirement, String requiredCapability, String preferredQualification, Processes processes,
		String url, Process currentProcess, CompletedSwitch completedSwitch) {
		super(id, companyName, team, location, contact, position, specificPosition, jobDescription, workType,
			employmentType, careerRequirement, requiredCapability, preferredQualification, processes, url);
		this.currentProcess = currentProcess;
		this.completedSwitch = completedSwitch;
	}

	public static Application simple(String companyName, String position, String url, Process process) {
		return new Application(null, companyName, null, null, null, position, null, null,
			null, null, null, null, null, Processes.initialFrom(process), url, process, new CompletedSwitch(false));
	}

	public static Application specify(Long id, String companyName, String team, String location, String contact,
		String position, String specificPosition, String jobDescription, String workType, EmploymentType employmentType,
		String careerRequirement, String requiredCapability, String preferredQualification, Processes processes,
		String url, Process currentProcess) {
		return new Application(id, companyName, team, location, contact, position, specificPosition, jobDescription,
			workType, employmentType, careerRequirement, requiredCapability, preferredQualification, processes, url,
			currentProcess, new CompletedSwitch(false));
	}

	public void updateSimply(String companyName, String position, String url, Long processId, LocalDateTime schedule) {
		processes.updateSchedule(processId, schedule);
		this.companyName = companyName;
		this.position = position;
		this.url = url;
	}

	public Application updateSpecifically(Application target) {
		return new Application(this.id, target.companyName, target.team, target.location, target.contact,
			target.position, target.specificPosition, target.jobDescription, target.workType, target.employmentType,
			target.careerRequirement, target.requiredCapability, target.preferredQualification, target.processes,
			target.url, target.currentProcess, target.completedSwitch);
	}

	public void updateCompleted(boolean isCompleted) {
		completedSwitch.convert(isCompleted);
	}

	public Process getProcessById(Long processId) {
		return processes.getValueById(processId);
	}

	public boolean isCompleted() {
		return completedSwitch.isCompleted();
	}
}
