package gohigher.application;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {

	APPLICATION_NOT_FOUND(404, "APPLICATION_001", "존재하지 않는 지원서입니다."),
	APPLICATION_PROCESS_NOT_FOUND(404, "APPLICATION_002", "존재하지 않는 전형입니다."),
	APPLICATION_ID_NULL(400, "APPLICATION_003", "지원서 id가 입력되지 않았습니다."),
	APPLICATION_PROCESS_ID_NULL(400, "APPLICATION_004", "지원서의 전형 id가 입력되지 않았습니다."),
	APPLICATION_PROCESS_TYPE_NULL(400, "APPLICATION_005", "지원서 전형의 타입이 입력되지 않았습니다."),
	APPLICATION_PROCESS_NOT_EXIST(400, "APPLICATION_006", "지원서 전형이 존재하지 않습니다."),

	INVALID_DATE_INFO(400, "APPLICATION_011", "잘못된 날짜 정보입니다."),
	INVALID_DATE_PATTERN(400, "APPLICATION_012", "잘못된 날짜 형식입니다."),
	CURRENT_PROCESS_NOT_FOUND(400, "APPLICATION_013", "현재 전형 단계를 조회하지 못했습니다."),

	ALREADY_VISIBLE_STATE_TO_CHANGE(400, "APPLICATION_020", "현재 보여주기/숨기기 상태로의 요청입니다."),

	INVALID_APPLICATION_SORTING_TYPE(400, "APPLICATION_021", "잘못된 sort 값입니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
