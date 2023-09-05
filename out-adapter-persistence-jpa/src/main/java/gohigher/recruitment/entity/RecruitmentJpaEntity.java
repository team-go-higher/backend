package gohigher.recruitment.entity;

import java.time.LocalDateTime;
import java.util.List;

import gohigher.common.EmploymentType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruitment")
@Entity
public class RecruitmentJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	@OneToMany(mappedBy = "recruitment")
	private List<RecruitmentProcessJpaEntity> processes;

	private boolean deleted;
}
