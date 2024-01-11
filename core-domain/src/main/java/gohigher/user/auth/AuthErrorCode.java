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
	NOT_EXISTED_TOKEN_TYPE(500, "AUTH_003", "제공되는 토큰 타입이 아닙니다."),
	NOT_EXISTED_TOKEN(404, "AUTH_004", "존재하지 않는 토큰입니다."),
	USED_REFRESH_TOKEN(400, "AUTH_005", "이미 사용된 리프레시 토큰입니다."),
	EXPIRED_REFRESH_TOKEN(400, "AUTH_006", "만료된 토큰입니다."),

	// 11 - 20 외부와의 정보 교환 부분에서 일어날 수 있는 에러 코드
	INVALID_OAUTH_RESPONSE(401, "AUTH_011", "소셜 로그인 실패입니다."),
	KAKAO_VALUE_IS_EMPTY(401, "AUTH_012", "카카오 사용자 정보가 존재하지 않습니다."),

	// 21 - 30 사용자 파라미터 관련 에러 코드
	INVALID_PROVIDER(400, "AUTH_021", "잘못된 provider 입니다."),

	// 31-40 리프레시 토큰 쿠키 관련 에러코드
	EMPTY_REFRESH_TOKEN_COOKIE(400, "AUTH_031", "리프레시 쿠키가 없습니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
