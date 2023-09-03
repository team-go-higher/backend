package gohigher.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GoHigherResponse<T> {

	private final boolean success;
	private final ErrorResponse error;
	private final T data;

	public static <T> GoHigherResponse<T> fail(String errorCode, String message) {
		ErrorResponse error = new ErrorResponse(errorCode, message);
		return new GoHigherResponse<>(false, error, null);
	}
}
