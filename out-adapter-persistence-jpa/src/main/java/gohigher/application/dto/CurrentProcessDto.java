package gohigher.application.dto;

import java.time.LocalDate;

import gohigher.application.CurrentProcess;

public interface CurrentProcessDto {

	Long getId();
	String getCompanyName();
	String getDuty();
	String getDetailedDuty();
	String getDescription();
	LocalDate getSchedule();

	CurrentProcess toDomain();
}
