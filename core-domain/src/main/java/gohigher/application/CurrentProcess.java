package gohigher.application;

import java.time.LocalDateTime;

import gohigher.common.ProcessType;
import lombok.Getter;

@Getter
public class CurrentProcess {

	private final Long id;
	private final String companyName;
	private final String duty;
	private final String detailedDuty;
	private final ProcessType type;
	private final String description;
	private final LocalDateTime schedule;

	public CurrentProcess(Long id, String companyName, String duty, String detailedDuty, ProcessType type,
		String description, LocalDateTime schedule) {
		ProcessType replacedProcessType = replaceNullToApply(type);
		this.id = id;
		this.companyName = companyName;
		this.duty = duty;
		this.detailedDuty = detailedDuty;
		this.type = replacedProcessType;
		this.description = description;
		this.schedule = schedule;
	}

	private ProcessType replaceNullToApply(ProcessType type) {
		if (type == null) {
			return ProcessType.TO_APPLY;
		}
		return type;
	}
}
