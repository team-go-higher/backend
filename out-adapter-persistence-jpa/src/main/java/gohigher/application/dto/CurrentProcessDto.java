package gohigher.application.dto;

import java.time.LocalDateTime;

import gohigher.common.ProcessType;

public interface CurrentProcessDto {

	Long getId();
	String getCompany_name();
	String getDuty();
	String getDetailed_duty();
	ProcessType getType();
	String getDescription();
	LocalDateTime getSchedule();
}
