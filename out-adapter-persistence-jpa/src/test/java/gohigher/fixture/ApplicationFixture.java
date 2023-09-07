package gohigher.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationFixture {

	NAVER_APPLICATION("Naver", "경기 성남시 분당구", "031-000-0000", "백엔드 개발", "", "", EmploymentType.PERMANENT, "", "", "",
		LocalDateTime.now(), List.of(), null, ""),
	KAKAO_APPLICATION("KAKAO", "경기 성남시 분당구", "031-000-0000", "백엔드 개발", "", "", EmploymentType.PERMANENT, "", "", "",
		LocalDateTime.now(), List.of(), null, ""),
	LINE_APPLICATION("LINE", "경기 성남시 분당구", "031-000-0000", "백엔드 개발", "", "", EmploymentType.PERMANENT, "", "", "",
		LocalDateTime.now(), List.of(), null, ""),
	COUPANG_APPLICATION("COUPANG", "서울특별시 송파구", "031-000-0000", "백엔드 개발", "", "", EmploymentType.PERMANENT, "", "", "",
		LocalDateTime.now(), List.of(), null, ""),
	;

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
	private final Process currentProcess;
	private final String url;

	public ApplicationJpaEntity toEntity(Long userId) {
		return new ApplicationJpaEntity(null, userId, companyName, location, contact, duty, jobDescription,
			workType, employmentType, careerRequirement, requiredCapability, preferredQualification, deadLine, url,
			0, new ArrayList<>(), null, false);
	}
}
