package gohigher.application.port.in;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import gohigher.application.ApplicationErrorCode;
import gohigher.global.exception.GoHigherException;
import lombok.Getter;

@Getter
public class DateApplicationRequest {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
		DatePattern.DATE.getFormat());

	private final Long userId;
	private final LocalDate date;

	public DateApplicationRequest(Long userId, String date) {
		this.userId = userId;
		this.date = convertToLocalDate(date);
	}

	private LocalDate convertToLocalDate(String date) {
		try {
			return LocalDate.parse(date, DATE_TIME_FORMATTER);
		} catch (DateTimeParseException e) {
			throw new GoHigherException(ApplicationErrorCode.INVALID_DATE_INFO);
		}
	}
}
