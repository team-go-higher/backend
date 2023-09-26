package gohigher.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

	NOT_CONTROLLED_ERROR(400, "GLOBAL_001", "다뤄지지 않은 에러입니다."),
	INVALID_JSON_FORMAT(400, "GLOBAL_002", "잘못된 형식의 요청 데이터입니다."),

	INPUT_EMPTY_ERROR(400, "GLOBAL_011", "빈 입력값입니다.");

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
