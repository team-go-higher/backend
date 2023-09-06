package gohigher.exception;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import gohigher.controller.response.ErrorResponse;
import gohigher.controller.response.GohigherResponse;
import gohigher.global.exception.GlobalErrorCode;
import gohigher.global.exception.GoHigherException;

@RestControllerAdvice
public class ControllerAdvice {

	private static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_CODE_DELIMITER = "||";
	private static final int METHOD_ARGUMENT_NOT_VALID_EXCEPTION_ERROR_INDEX = 0;
	private static final int METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_INDEX = 1;

	@ExceptionHandler(GoHigherException.class)
	public ResponseEntity<GohigherResponse<Void>> handleGoHigherException(GoHigherException e) {
		int statusCode = e.getStatusCode();
		GohigherResponse<Void> response = GohigherResponse.fail(e.getErrorCode(), e.getMessage());

		return ResponseEntity.status(statusCode).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GohigherResponse<ErrorResponse>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		List<FieldError> fieldErrors = e.getFieldErrors();
		FieldError firstFieldError = fieldErrors.get(0);
		String[] errorCodeAndMessage = firstFieldError.getDefaultMessage()
			.split(METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_CODE_DELIMITER);

		GohigherResponse<ErrorResponse> errorResponse = GohigherResponse.fail(
			errorCodeAndMessage[METHOD_ARGUMENT_NOT_VALID_EXCEPTION_ERROR_INDEX],
			errorCodeAndMessage[METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE_INDEX]
		);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GohigherResponse<Void>> uncontrolledException(Exception e) {
		GlobalErrorCode errorCode = GlobalErrorCode.NOT_CONTROLLED_ERROR;
		int statusCode = errorCode.getStatusCode();
		GohigherResponse<Void> response = GohigherResponse.fail(errorCode.getErrorCode(), errorCode.getMessage());

		return ResponseEntity.status(statusCode).body(response);
	}
}
