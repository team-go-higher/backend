package gohigher.pagination;

import gohigher.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaginationErrorCode implements ErrorCode {

	PAGE_UNDER_ONE(400, "PAGINATION_001", "page 는 1 이상이어야 합니다."),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;
}
