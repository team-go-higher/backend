package gohigher.application.entity;

import static gohigher.application.ApplicationErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import gohigher.application.Application;
import gohigher.application.CompletedSwitch;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import gohigher.common.Processes;
import gohigher.global.exception.GoHigherException;
import gohigher.recruitment.entity.RecruitmentJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "application")
@Entity
public class ApplicationJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private String companyName;
	private String team;
	private String location;
	private String contact;
	private String position;
	private String specificPosition;
	private String jobDescription;
	private String workType;

	@Enumerated(value = EnumType.STRING)
	private EmploymentType employmentType;
	private String careerRequirement;
	private String requiredCapability;
	private String preferredQualification;
	private String url;

	@Builder.Default
	@OneToMany(mappedBy = "application")
	private List<ApplicationProcessJpaEntity> processes = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruitment_id")
	private RecruitmentJpaEntity recruitment;

	private boolean deleted;
	private Boolean isCompleted;

	public static ApplicationJpaEntity of(Application application, Long userId) {
		return new ApplicationJpaEntity(null,
			userId,
			application.getCompanyName(),
			application.getTeam(),
			application.getLocation(),
			application.getContact(),
			application.getPosition(),
			application.getSpecificPosition(),
			application.getJobDescription(),
			application.getWorkType(),
			application.getEmploymentType(),
			application.getCareerRequirement(),
			application.getRequiredCapability(),
			application.getPreferredQualification(),
			application.getUrl(),
			new ArrayList<>(),
			null,
			false,
			application.isCompleted()
		);
	}

	public void addProcess(ApplicationProcessJpaEntity process) {
		if (process.getApplication() == null) {
			process.assignApplication(this);
		}

		processes.add(process);
	}

	public void delete() {
		this.deleted = true;
	}

	public Application toDomain() {
		List<Process> processes = getProcessList();
		return createApplication(processes, findCurrentProcess(processes));
	}

	public Application toCalenderDomain() {
		List<Process> processes = getProcessList();
		return createApplication(processes, null);
	}

	public Application toKanbanDomain() {
		List<Process> processes = getProcessList();
		return createApplication(processes, findCurrentProcess(processes));
	}

	private Process findCurrentProcess(List<Process> processes) {
		ApplicationProcessJpaEntity currentProcess = getProcesses().stream()
			.filter(ApplicationProcessJpaEntity::isCurrent)
			.findAny()
			.orElseThrow(() -> new GoHigherException(CURRENT_PROCESS_NOT_FOUND));

		return processes.stream()
			.filter(process -> process.getType() == currentProcess.getType())
			.filter(process -> process.getOrder() == currentProcess.getOrder())
			.findAny()
			.orElseThrow(() -> new GoHigherException(CURRENT_PROCESS_NOT_FOUND));
	}

	private List<Process> getProcessList() {
		return processes.stream()
			.map(ApplicationProcessJpaEntity::toDomain)
			.toList();
	}

	private Application createApplication(List<Process> processes, Process currentProcess) {
		return new Application(id, companyName, team, location, contact, position, specificPosition, jobDescription,
			workType, employmentType, careerRequirement, requiredCapability, preferredQualification,
			Processes.of(processes), url, currentProcess, new CompletedSwitch(isCompleted));
	}

	public void update(Application application) {
		this.companyName = application.getCompanyName();
		this.team = application.getTeam();
		this.location = application.getLocation();
		this.contact = application.getContact();
		this.position = application.getPosition();
		this.specificPosition = application.getSpecificPosition();
		this.jobDescription = application.getJobDescription();
		this.workType = application.getWorkType();
		this.employmentType = application.getEmploymentType();
		this.careerRequirement = application.getCareerRequirement();
		this.requiredCapability = application.getRequiredCapability();
		this.preferredQualification = application.getPreferredQualification();
		this.url = application.getUrl();
		this.isCompleted = application.isCompleted();
	}

	public void resetProcesses() {
		this.processes = new ArrayList<>();
	}
}
