package gohigher.position;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionErrorCode implements ErrorCode {

	EXIST_ALREADY_POSITION(400, "POSITION_011", "목록에 존재하는 포지션입니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
