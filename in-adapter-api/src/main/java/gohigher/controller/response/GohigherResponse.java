package gohigher.controller.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GohigherResponse<T> {

	private final boolean success;
	private final ErrorResponse error;
	private final T data;

	public static <T> GohigherResponse<T> success(T data) {
		return new GohigherResponse<>(true, null, data);
	}

	public static <T> GohigherResponse<T> fail(String errorCode, String message) {
		ErrorResponse error = new ErrorResponse(errorCode, message);
		return new GohigherResponse<>(false, error, null);
	}
}
