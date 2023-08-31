package gohigher.global.exception;

import lombok.Getter;

@Getter
public class GoHigherException extends RuntimeException {

	private final int statusCode;
	private final String errorCode;
	private final String message;

	public GoHigherException(final ErrorCode code) {
		statusCode = code.getStatusCode();
		errorCode = code.getErrorCode();
		message = code.getMessage();
	}
}
