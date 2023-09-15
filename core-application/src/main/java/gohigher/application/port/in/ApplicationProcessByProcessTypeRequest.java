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

	@NotNull
	private Long applicationId;

	@NotNull
	private ProcessType processType;
}
