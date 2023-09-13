package gohigher.application.port.in;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import gohigher.application.ApplicationErrorCode;
import gohigher.global.exception.GoHigherException;
import lombok.Getter;

@Getter
public class DateApplicationRequest {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(Pattern.DATE.getFormat());

	private final Long userId;
	private final LocalDate date;

	public DateApplicationRequest(Long userId, String dateStr) {
		this.userId = userId;
		this.date = convertToLocalDate(dateStr);
	}

	private LocalDate convertToLocalDate(String dateStr) {
		try {
			return LocalDate.parse(dateStr, DATE_TIME_FORMATTER);
		} catch (DateTimeParseException e) {
			throw new GoHigherException(ApplicationErrorCode.INVALID_DATE_INFO);
		}
	}
}
