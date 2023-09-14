package gohigher.application.entity;

import java.util.Comparator;
import java.util.List;

import gohigher.application.Application;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
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
	private String duty;
	private String position;
	private String jobDescription;
	private String workType;

	@Enumerated(value = EnumType.STRING)
	private EmploymentType employmentType;
	private String careerRequirement;
	private String requiredCapability;
	private String preferredQualification;
	private String url;
	private int currentProcessOrder;

	@OneToMany(mappedBy = "application")
	private List<ApplicationProcessJpaEntity> processes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruitment_id")
	private RecruitmentJpaEntity recruitment;

	private boolean deleted;

	public static ApplicationJpaEntity from(Application application) {
		List<Process> processes = application.getProcesses();
		return new ApplicationJpaEntity(null,
			application.getUserId(),
			application.getCompanyName(),
			application.getTeam(),
			application.getLocation(),
			application.getContact(),
			application.getDuty(),
			application.getPosition(),
			application.getJobDescription(),
			application.getWorkType(),
			application.getEmploymentType(),
			application.getCareerRequirement(),
			application.getRequiredCapability(),
			application.getPreferredQualification(),
			application.getUrl(),
			FIRST_PROCESS_ORDER,
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

	public Application toDomain() {
		List<Process> processes = this.processes.stream()
			.sorted(Comparator.comparingInt(ApplicationProcessJpaEntity::getOrder))
			.map(ApplicationProcessJpaEntity::toDomain)
			.toList();

		return new Application(id, companyName, team, location, contact, duty, position, jobDescription, workType,
			employmentType, careerRequirement, requiredCapability, preferredQualification, processes, url, userId,
			processes.get(currentProcessOrder));
	}
}
