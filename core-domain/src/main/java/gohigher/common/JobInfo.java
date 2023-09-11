package gohigher.common;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * companyName            회사명
 * team					  부서
 * duty                   직무
 * position				  포지션
 * processes              전형 단계
 * deadLine               전형 마감일(상시면 해당 년의 마지막 날로 설정)
 * jobDescription         주요 업무
 * requiredCapability     필수 역량
 * url                    공고 URL
 * location               회사 위치
 * preferredQualification  우대사항
 * contact                채용 담당자 연락처
 * employmentType         고용 형태
 * careerRequirement      경력 조건
 * workType               근무 형태
 */
@Getter
@RequiredArgsConstructor
public abstract class JobInfo {

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
	private final String url;
}
