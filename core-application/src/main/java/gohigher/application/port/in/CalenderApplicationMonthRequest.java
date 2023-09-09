package gohigher.application.port.in;

import static gohigher.application.ApplicationErrorCode.*;

import gohigher.global.exception.GoHigherException;
import lombok.Getter;

@Getter
public class CalenderApplicationMonthRequest {

	private final Long userId;
	private final int year;
	private final int month;

	public CalenderApplicationMonthRequest(Long userId, int year, int month) {
		validateNegativeYear(year);
		verifyMonthIsInValidRange(month);
		this.userId = userId;
		this.year = year;
		this.month = month;
	}

	private void validateNegativeYear(int year) {
		if (year <= 0) {
			throw new GoHigherException(INVALID_DATE_INFO);
		}
	}

	private void verifyMonthIsInValidRange(int month) {
		if (month < 1 || month > 12) {
			throw new GoHigherException(INVALID_DATE_INFO);
		}
	}
}
