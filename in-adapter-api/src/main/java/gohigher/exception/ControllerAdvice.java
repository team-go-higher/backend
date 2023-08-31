package gohigher.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import gohigher.controller.response.ApiResponse;
import gohigher.global.exception.GoHigherException;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(GoHigherException.class)
	public ResponseEntity<ApiResponse<Void>> handleGoHigherException(final GoHigherException e) {
		int statusCode = e.getStatusCode();
		ApiResponse<Void> response = ApiResponse.fail(e.getErrorCode(), e.getMessage());

		return ResponseEntity.status(statusCode).body(response);
	}
}
