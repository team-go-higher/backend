package gohigher.application.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DatePattern {
	DATE("yyyy-MM-dd"),
	DATE_TIME("yyyy-MM-dd'T'HH:mm:ss"),
	;

	private final String format;
}
