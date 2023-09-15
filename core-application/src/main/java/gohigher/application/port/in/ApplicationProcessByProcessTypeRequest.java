package gohigher.application.port.in;

import gohigher.common.ProcessType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationProcessByProcessTypeRequest {

	@NotNull(message = "APPLICATION_003||지원서 id가 입력되지 않았습니다.")
	private Long applicationId;

	@NotNull(message = "APPLICATION_005||지원서 전형의 타입이 입력되지 않았습니다.")
	private ProcessType processType;
}
