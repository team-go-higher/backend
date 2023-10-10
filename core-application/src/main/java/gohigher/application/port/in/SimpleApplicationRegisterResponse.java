package gohigher.application.port.in;

import java.time.LocalDateTime;

import gohigher.application.Application;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SimpleApplicationRegisterResponse {

	private final Long id;
	private final String companyName;
	private final LocalDateTime currentProcessSchedule;
	private final String currentProcessDescription;

	public static SimpleApplicationRegisterResponse from(Application application) {
		return new SimpleApplicationRegisterResponse(application.getId(), application.getCompanyName(),
			application.getCurrentProcess().getSchedule(), application.getCurrentProcess().getDescription());

	}
}
