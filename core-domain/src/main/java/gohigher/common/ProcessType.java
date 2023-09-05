package gohigher.common;

import static gohigher.common.JobInfoErrorCode.*;

import java.util.Arrays;

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
			.orElseThrow(() -> new GoHigherException(APPLICATION_NOT_EXIST));
	}
}
