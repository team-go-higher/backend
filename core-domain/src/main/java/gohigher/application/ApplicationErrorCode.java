package gohigher.application;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {

	APPLICATION_NOT_FOUND(400, "APPLICATION_001", "존재하지 않는 지원서입니다."),
	APPLICATION_PROCESS_NOT_FOUND(404, "APPLICATION_002", "지원서에서 존재하지 않는 ProcessType 입니다.")
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
