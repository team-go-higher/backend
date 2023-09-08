package gohigher.application.port.in;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CurrentProcessUpdateRequest {

	private Long applicationId;
	private String currentProcessType;
}
