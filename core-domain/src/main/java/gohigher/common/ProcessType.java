package gohigher.common;

import java.util.Arrays;

public enum ProcessType {
	TO_APPLY,
	DOCUMENT,
	TEST,
	INTERVIEW,
	COMPLETE,
	;

	public static ProcessType from(String value) {
		return Arrays.stream(values())
			.filter(processType -> processType.name().equals(value))
			.findAny()
			.orElseThrow(IllegalArgumentException::new);
	}
}
