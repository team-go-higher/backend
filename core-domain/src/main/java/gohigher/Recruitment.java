package gohigher;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @param companyName            회사명
 * @param location               회사 위치
 * @param contact                인사팀 연락처
 * @param duty                   직무
 * @param jobDescription         담당 업무
 * @param workType               근무 형태
 * @param employmentType         고용형태
 * @param careerRequirement      경력 조건
 * @param requiredCapability     필수 역량
 * @param preferredQualification 우대사항
 * @param deadLine               마감 날짜(상시면 해당 년의 마지막 날로 설정)
 * @param processes              공고 과정
 * @param url                    공고 URL
 */
public record Recruitment(
        String companyName,
        String location,
        String contact,
        String duty,
        String jobDescription,
        String workType,
        EmploymentType employmentType,
        String careerRequirement,
        String requiredCapability,
        String preferredQualification,
        LocalDateTime deadLine,
        List<NoticeProcess> processes,
        String url
) {
}
