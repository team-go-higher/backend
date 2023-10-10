package gohigher.position;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionErrorCode implements ErrorCode {

	POSITION_NOT_EXISTS(400, "POSITION_001", "존재하지 않는 직무입니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
