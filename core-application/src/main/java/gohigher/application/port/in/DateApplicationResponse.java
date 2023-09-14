package gohigher.application.port.in;

import gohigher.application.Application;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DateApplicationResponse {

	private final Long applicationId;
	private final String companyName;
	private final ProcessResponse process;

	public static DateApplicationResponse of(Application application, ProcessResponse process) {
		return new DateApplicationResponse(application.getId(), application.getCompanyName(), process);
	}
}
