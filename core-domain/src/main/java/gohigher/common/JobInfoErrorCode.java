package gohigher.common;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobInfoErrorCode implements ErrorCode {


	APPLICATION_NOT_EXIST(400, "JOB_INFO_001", "존재하지 않는 전형 단계입니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
