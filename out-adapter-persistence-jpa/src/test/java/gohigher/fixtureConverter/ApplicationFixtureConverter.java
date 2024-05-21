package gohigher.fixtureConverter;

import java.util.ArrayList;

import gohigher.application.Application;
import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.common.Process;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationFixtureConverter {

	public static ApplicationJpaEntity convertToApplicationEntity(Long userId, Application application) {
		return createApplicationJpaEntity(userId, application);
	}

	private static ApplicationJpaEntity createApplicationJpaEntity(
		Long userId, Application application) {
		return new ApplicationJpaEntity(null, userId, application.getCompanyName(), application.getTeam(),
			application.getLocation(), application.getContact(), application.getPosition(),
			application.getSpecificPosition(), application.getJobDescription(), application.getWorkType(),
			application.getEmploymentType(), application.getCareerRequirement(), application.getRequiredCapability(),
			application.getPreferredQualification(), application.getUrl(), new ArrayList<>(), null, false, false);
	}

	public static ApplicationProcessJpaEntity convertToApplicationProcessEntity(
		ApplicationJpaEntity applicationJpaEntity, Process process, boolean isCurrent) {
		return ApplicationProcessJpaEntity.of(applicationJpaEntity, process, isCurrent);
	}

	public static ApplicationProcessJpaEntity convertToApplicationProcessEntity(
		ApplicationJpaEntity applicationJpaEntity, Process process, Process currentProcess) {
		return convertToApplicationProcessEntity(applicationJpaEntity, process, currentProcess == process);
	}
}
