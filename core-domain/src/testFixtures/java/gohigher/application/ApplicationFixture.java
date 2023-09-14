package gohigher.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import gohigher.common.EmploymentType;
import gohigher.common.Process;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationFixture {

	NAVER_APPLICATION("Naver", "파이낸셜", "경기 성남시 분당구", "031-000-0000", "백엔드 개발", "", "", "", EmploymentType.PERMANENT, "",
		"", "", LocalDateTime.now(), new ArrayList<>(), null, ""),
	KAKAO_APPLICATION("KAKAO", "페이", "경기 성남시 분당구", "031-000-0000", "백엔드 개발", "", "", "", EmploymentType.PERMANENT, "",
		"", "", LocalDateTime.now(), new ArrayList<>(), null,  ""),
	LINE_APPLICATION("LINE", "메신저", "경기 성남시 분당구", "031-000-0000", "백엔드 개발", "", "", "", EmploymentType.PERMANENT, "",
		"", "", LocalDateTime.now(), new ArrayList<>(), null,  ""),
	COUPANG_APPLICATION("COUPANG", "로켓배송", "서울특별시 송파구", "031-000-0000", "백엔드 개발", "", "", "", EmploymentType.PERMANENT,
		"", "", "", LocalDateTime.now(), new ArrayList<>(), null, ""),
	;

	private final String companyName;
	private final String team;
	private final String location;
	private final String contact;
	private final String duty;
	private final String position;
	private final String jobDescription;
	private final String workType;
	private final EmploymentType employmentType;
	private final String careerRequirement;
	private final String requiredCapability;
	private final String preferredQualification;
	private final LocalDateTime deadLine;
	private final List<Process> processes;
	private final Process currentProcess;
	private final String url;

	public Application toDomain() {
		return new Builder(this).toDomain();
	}

	public Application toDomain(List<Process> processes, Process currentProcess) {
		return new Builder(this)
			.processes(processes)
			.currentProcess(currentProcess)
			.toDomain();
	}

	@NoArgsConstructor
	public static class Builder {
		private String companyName;
		private String team;
		private String location;
		private String contact;
		private String duty;
		private String position;
		private String jobDescription;
		private String workType;
		private EmploymentType employmentType;
		private String careerRequirement;
		private String requiredCapability;
		private String preferredQualification;
		private LocalDateTime deadLine;
		private List<Process> processes;
		private Process currentProcess;
		private String url;

		public Builder(ApplicationFixture application) {
			this.companyName = application.getCompanyName();
			this.team = application.getTeam();
			this.location = application.getLocation();
			this.contact = application.getContact();
			this.duty = application.getDuty();
			this.position = application.getPosition();
			this.jobDescription = application.getJobDescription();
			this.workType = application.getWorkType();
			this.employmentType = application.getEmploymentType();
			this.careerRequirement = application.getCareerRequirement();
			this.requiredCapability = application.getRequiredCapability();
			this.preferredQualification = application.getPreferredQualification();
			this.deadLine = application.getDeadLine();
			this.processes = application.getProcesses();
			this.currentProcess = application.getCurrentProcess();
			this.url = application.getUrl();
		}

		public Builder processes(List<Process> processes) {
			this.processes = processes;
			return this;
		}

		public Builder currentProcess(Process currentProcess) {
			this.currentProcess = currentProcess;
			return this;
		}

		public Application toDomain() {
			return new Application(null, companyName, team, location, contact, duty, position, jobDescription,
				workType, employmentType, careerRequirement, requiredCapability, preferredQualification, deadLine,
				processes, url, currentProcess);
		}
	}
}
