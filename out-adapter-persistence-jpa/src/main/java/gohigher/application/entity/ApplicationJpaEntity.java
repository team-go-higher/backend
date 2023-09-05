package gohigher.application.entity;

import java.time.LocalDateTime;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private String companyName;
	private String location;
	private String contact;
	private String duty;
	private String jobDescription;
	private String workType;

	@Enumerated(value = EnumType.STRING)
	private EmploymentType employmentType;
	private String careerRequirement;
	private String requiredCapability;
	private String preferredQualification;
	private LocalDateTime deadline;
	private String url;
	private int currentProcess;

	@OneToMany(mappedBy = "application")
	private List<ApplicationProcessJpaEntity> processes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recruitment_id")
	private RecruitmentJpaEntity recruitment;

	private boolean deleted;

	public static ApplicationJpaEntity of(Long userId, Application application) {
		List<Process> processes = application.getProcesses();
		int currentProcessIndex = processes.indexOf(application.getCurrentProcess());
		return new ApplicationJpaEntity(null,
			userId,
			application.getCompanyName(),
			application.getLocation(),
			application.getContact(),
			application.getDuty(),
			application.getJobDescription(),
			application.getWorkType(),
			application.getEmploymentType(),
			application.getCareerRequirement(),
			application.getRequiredCapability(),
			application.getPreferredQualification(),
			application.getDeadLine(),
			application.getUrl(),
			currentProcessIndex,
			null,
			null,
			false
		);
	}
}
