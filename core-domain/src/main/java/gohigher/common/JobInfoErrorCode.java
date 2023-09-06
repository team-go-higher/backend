package gohigher.common;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobInfoErrorCode implements ErrorCode {

	INVALID_PROCESS_TYPE(400, "JOB_INFO_001", "유효하지 않은 전형 단계입니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
