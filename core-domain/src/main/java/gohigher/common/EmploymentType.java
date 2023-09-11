package gohigher.common;

import static gohigher.common.JobInfoErrorCode.*;

import java.util.Arrays;

import gohigher.global.exception.GoHigherException;

public enum EmploymentType {

	INTERN,
	PERMANENT,
	TEMPORARY,
	;

	public static EmploymentType from(String value) {
		return Arrays.stream(values())
			.filter(employmentType -> employmentType.name().equals(value))
			.findAny()
			.orElseThrow(() -> new GoHigherException(INVALID_EMPLOYMENT_TYPE));
	}
}
