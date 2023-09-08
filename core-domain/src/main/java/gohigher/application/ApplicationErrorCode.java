package gohigher.application;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {

	APPLICATION_NOT_FOUND(404, "APPLICATION_001", "존재하지 않는 지원서입니다."),
	APPLICATION_PROCESS_NOT_FOUND(404, "APPLICATION_002", "지원서에서 존재하지 않는 ProcessType 입니다."),
	APPLICATION_FORBIDDEN(403, "APPLICATION_003", "지원서에 대한 수정 권한이 없습니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
