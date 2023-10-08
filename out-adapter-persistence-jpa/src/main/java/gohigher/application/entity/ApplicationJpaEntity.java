package gohigher.application.entity;

import java.util.Comparator;
import java.util.List;

import gohigher.application.Application;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import gohigher.common.Processes;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "application")
@Entity
public class ApplicationJpaEntity {

	private static final int FIRST_PROCESS_ORDER = 0;

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

	@Enumerated(value = EnumType.STRING)
	private ProcessType currentProcessType;
	private int currentProcessOrder;

	@OneToMany(mappedBy = "application")
	private List<ApplicationProcessJpaEntity> processes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruitment_id")
	private RecruitmentJpaEntity recruitment;

	private boolean deleted;

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
			application.getCurrentProcess().getType(),
			application.getCurrentProcess().getOrder(),
			null,
			null,
			false
		);
	}

	public void addProcess(ApplicationProcessJpaEntity process) {
		if (process.getApplication() == null) {
			process.assignApplication(this);
		}

		processes.add(process);
	}

	public void changeToDelete() {
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
		return processes.stream()
			.filter(process -> process.getType() == currentProcessType)
			.filter(process -> process.getOrder() == currentProcessOrder)
			.findAny()
			.orElse(null);
	}

	private List<Process> getProcessList() {
		return processes.stream()
			.sorted(Comparator.comparingInt(ApplicationProcessJpaEntity::getOrder))
			.map(ApplicationProcessJpaEntity::toDomain)
			.toList();
	}

	private Application createApplication(List<Process> processes, Process currentProcess) {
		return new Application(id, companyName, team, location, contact, position, specificPosition, jobDescription,
			workType, employmentType, careerRequirement, requiredCapability, preferredQualification,
			Processes.of(processes), url, currentProcess);
	}
}
