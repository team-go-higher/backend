package gohigher.application;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CurrentProcess {

	private final Long id;
	private final String companyName;
	private final String duty;
	private final String detailedDuty;
	private final String processDescription;
	private final LocalDate schedule;
}
