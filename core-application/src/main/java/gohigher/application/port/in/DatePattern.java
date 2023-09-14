package gohigher.application.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DatePattern {
	DATE("yyyy-MM-dd"),
	;

	private final String format;
}
