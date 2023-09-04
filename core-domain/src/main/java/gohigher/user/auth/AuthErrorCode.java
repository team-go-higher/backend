package gohigher.user.auth;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {

	// 1 - 10 내부 토큰에서 일어날 수 있는 에러 코드
	TOKEN_EXPIRED(401, "AUTH_001", "만료된 토큰입니다."),
	INVALID_TOKEN_BY_SIGNATURE(401, "AUTH_002", "잘못된 토큰입니다."),

	// 11 - 20 외부와의 정보 교환 부분에서 일어날 수 있는 에러 코드
	INVALID_OAUTH_RESPONSE(401, "AUTH_010", "소셜 로그인 실패입니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
