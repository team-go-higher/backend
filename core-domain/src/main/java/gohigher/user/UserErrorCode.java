package gohigher.user;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

	USER_NOT_EXISTS(404, "USER_001", "존재하지 않는 사용자입니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}

