package gohigher.entity.recruitment;

import java.time.LocalDateTime;

import gohigher.recruitment.EmploymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecruitmentDetailJpaEntity {

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
    @Column(updatable = false)
    private EmploymentType employmentType;
    private String careerRequirement;
    private String requiredCapability;
    private String preferredQualification;
    private LocalDateTime deadLine;
    private String url;
    private boolean isDeleted;
}
