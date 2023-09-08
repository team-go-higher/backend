package gohigher.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentProcessUpdateRequest {

	private Long applicationId;
	private String currentProcessType;
}
