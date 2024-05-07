package gohigher.application.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CompletedUpdatingResponse {

	private final Long applicationId;
	private final Boolean isCompleted;
}
