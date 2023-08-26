package gohigher.application;

import java.time.LocalDateTime;
import java.util.List;

import gohigher.common.EmploymentType;
import gohigher.common.JobInfo;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import lombok.Getter;

/**
 * currentProcess   현재 프로세스
 */
@Getter
public class Application extends JobInfo {

	private final Process currentProcess;

	public Application(String companyName, String location, String contact, String duty, String jobDescription,
		String workType, EmploymentType employmentType, String careerRequirement, String requiredCapability,
		String preferredQualification, LocalDateTime deadLine, List<Process> processes,
		String url, Process currentProcess) {
		super(companyName, location, contact, duty, jobDescription, workType, employmentType, careerRequirement,
			requiredCapability, preferredQualification, deadLine, processes, url);
		this.currentProcess = currentProcess;
	}

	public static Application simple(String companyName, String duty, ProcessType processType, LocalDateTime schedule) {
		Process process = new Process(processType, null, schedule);
		return new Application(companyName, null, null, duty, null, null, null, null, null, null, null,
			List.of(process), null, process);
	}
}
