package gohigher.application.port.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentProcessUpdateRequest {

	@NotNull(message = "APPLICATION_004||지원서 id가 입력되지 않았습니다.")
	private Long applicationId;

	@NotNull(message = "APPLICATION_005||지원서의 전형 id가 입력되지 않았습니다.")
	private Long processId;
}
