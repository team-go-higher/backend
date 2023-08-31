package gohigher.controller.response;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

	private final boolean success;
	private final ErrorResponse error;
	private final T data;

	public static <T> ApiResponse<T> success(final T data) {
		return new ApiResponse<>(true, null, data);
	}

	public static <T> ApiResponse<T> fail(final String errorCode, final String message) {
		ErrorResponse error = new ErrorResponse(errorCode, message);
		return new ApiResponse<>(false, error, null);
	}
}
