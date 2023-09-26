package gohigher.application.port.in;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagingRequest {

	@Min(value = 1, message = "PAGINATION_001||page 는 1 이상이어야 합니다.")
	private final int page;
	@Min(value = 1, message = "PAGINATION_002||size 는 1 이상이어야 합니다.")
	private final int size;
}
