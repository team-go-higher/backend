package gohigher.common;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * companyName            회사명
 * location               회사 위치
 * contact                인사팀 연락처
 * duty                   직무
 * jobDescription         담당 업무
 * workType               근무 형태
 * employmentType         고용형태
 * careerRequirement      경력 조건
 * requiredCapability     필수 역량
 * preferredQualification 우대사항
 * deadLine               마감 날짜(상시면 해당 년의 마지막 날로 설정)
 * processes              공고 과정
 * url                    공고 URL
 */
@AllArgsConstructor
@Getter
public abstract class JobInfo {
	private final String companyName;
	private final String location;
	private final String contact;
	private final String duty;
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
