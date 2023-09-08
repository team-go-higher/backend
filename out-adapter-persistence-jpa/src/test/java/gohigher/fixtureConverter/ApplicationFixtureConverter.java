package gohigher.fixtureConverter;

import java.util.ArrayList;

import gohigher.application.Application;
import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.common.Process;

public class ApplicationFixtureConverter {

	public static ApplicationJpaEntity convertToApplicationEntity(Long userId, Application application) {
		return new ApplicationJpaEntity(null, userId, application.getCompanyName(), application.getLocation(),
			application.getContact(), application.getDuty(), application.getJobDescription(),
			application.getWorkType(), application.getEmploymentType(), application.getCareerRequirement(),
			application.getRequiredCapability(),
			application.getPreferredQualification(), application.getDeadline(), application.getUrl(),
			0, new ArrayList<>(), null, false);
	}

	public static ApplicationProcessJpaEntity convertToApplicationProcessEntity(
		ApplicationJpaEntity applicationJpaEntity, Process process, int order) {
		return ApplicationProcessJpaEntity.of(applicationJpaEntity, process, order);
	}
}
