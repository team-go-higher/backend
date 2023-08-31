package gohigher.presentation.response;

import gohigher.global.exception.GoHigherException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiResponse<T> {

	private final boolean success;
	private final ErrorResponse error;
	private final T data;

	public static <T> ApiResponse<T> success(final T data) {
		return new ApiResponse<>(true, null, data);
	}

	public static <T> ApiResponse<T> fail(final GoHigherException exception) {
		ErrorResponse error = new ErrorResponse(exception.getErrorCode(), exception.getMessage());
		return new ApiResponse<>(false, error, null);
	}
}
