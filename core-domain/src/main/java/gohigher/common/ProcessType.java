package gohigher.common;

import static gohigher.common.JobInfoErrorCode.*;

import java.util.Arrays;
import java.util.List;

import gohigher.global.exception.GoHigherException;

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
			.orElseThrow(() -> new GoHigherException(INVALID_PROCESS_TYPE));
	}

	public static List<ProcessType> from(List<String> values) {
		if (values == null) {
			return null;
		}

		return values.stream()
			.map(ProcessType::from)
			.toList();
	}
}
