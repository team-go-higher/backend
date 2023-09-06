package gohigher.common;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobInfoErrorCode implements ErrorCode {

	INVALID_PROCESS_TYPE(400, "JOB_INFO_001", "유효하지 않은 전형 단계입니다."),
	COMPANY_NAME_BLANK(400, "JOB_INFO_002", "회사명이 입력되지 않았습니다."),
	DUTY_BLANK(400, "JOB_INFO_003", "직무가 입력되지 않았습니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
