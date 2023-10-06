package gohigher.application.port.in;

import java.time.LocalDateTime;

public class SimpleApplicationRegisterResponse {

	private final Long id;
	private final String companyName;
	private final LocalDateTime currentProcessSchedule;
	private final String currentProcessDescription;

	public SimpleApplicationRegisterResponse(Long id, String companyName, LocalDateTime currentProcessSchedule,
		String currentProcessDescription) {
		this.id = id;
		this.companyName = companyName;
		this.currentProcessSchedule = currentProcessSchedule;
		this.currentProcessDescription = currentProcessDescription;
	}
}
