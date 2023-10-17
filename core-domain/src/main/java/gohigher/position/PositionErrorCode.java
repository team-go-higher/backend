package gohigher.position;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionErrorCode implements ErrorCode {

	POSITION_NOT_EXISTS(404, "POSITION_001", "존재하지 않는 직무입니다."),

	EMPTY_INPUT_POSITION_ID(400, "POSITION_011", "희망 직무 ID가 비어있습니다."),
	DUPLICATED_POSITION(400, "POSITION_012", "중복된 직무 ID를 입력받았습니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
