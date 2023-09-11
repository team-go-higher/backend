package gohigher.application;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {

	APPLICATION_NOT_FOUND(404, "APPLICATION_001", "존재하지 않는 지원서입니다."),
	APPLICATION_PROCESS_NOT_FOUND(404, "APPLICATION_002", "지원서에 존재하지 않는 전형입니다."),
	APPLICATION_FORBIDDEN(403, "APPLICATION_003", "지원서에 대한 수정 권한이 없습니다."),
	APPLICATION_ID_NULL(403, "APPLICATION_004", "지원서 id가 입력되지 않았습니다."),
	APPLICATION_PROCESS_ID_NULL(403, "APPLICATION_005", "지원서의 전형 id가 입력되지 않았습니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
