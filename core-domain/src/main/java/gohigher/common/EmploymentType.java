package gohigher.common;

import static gohigher.common.JobInfoErrorCode.*;

import java.util.Arrays;

import gohigher.global.exception.GoHigherException;

public enum EmploymentType {

	INTERN,
	PERMANENT,
	TEMPORARY,
	UNDEFINED,
	;

	public static EmploymentType from(String value) {
		if (value == null) {
			return UNDEFINED;
		}

		return Arrays.stream(values())
			.filter(employmentType -> employmentType.name().equals(value))
			.findAny()
			.orElseThrow(() -> new GoHigherException(INVALID_EMPLOYMENT_TYPE));
	}

	public String getValue() {
		if (this.equals(UNDEFINED)) {
			return null;
		}

		return this.name();
	}
}
