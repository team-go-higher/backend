package gohigher.application.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpdatedVisibilityResponse {

	private final Long applicationId;
	private final Boolean isVisible;
}
