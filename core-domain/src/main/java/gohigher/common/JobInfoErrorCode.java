package gohigher.common;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobInfoErrorCode implements ErrorCode {

	INVALID_PROCESS_TYPE(400, "JOB_INFO_001", "유효하지 않은 전형 단계입니다."),
	COMPANY_NAME_BLANK(400, "JOB_INFO_002", "회사명이 입력되지 않았습니다."),
	POSITION_BLANK(400, "JOB_INFO_003", "직무가 입력되지 않았습니다."),
	INVALID_EMPLOYMENT_TYPE(400, "JOB_INFO_004", "유효하지 않은 고용 형태입니다."),
	PROCESS_TYPE_BLANK(400, "JOB_INFO_005", "전형 단계가 입력되지 않았습니다."),
	PROCESS_DESCRIPTION_BLANK(400, "JOB_INFO_006", "세부 전형이 입력되지 않았습니다."),
	PROCESS_SCHEDULE_NULL(400, "JOB_INFO_007", "전형 일정이 입력되지 않았습니다."),
	APPLICATION_INFO_NULL(400, "JOB_INFO_008", "지원서 정보가 입력되지 않았습니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
