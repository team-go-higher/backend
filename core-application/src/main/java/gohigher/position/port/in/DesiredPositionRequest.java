package gohigher.position.port.in;

import java.util.List;

import org.springframework.util.CollectionUtils;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DesiredPositionRequest {

	@NotEmpty(message = "GLOBAL_011||빈 입력값입니다.")
	private List<Long> positionIds;

	@AssertTrue(message = "GLOBAL_011||빈 입력값입니다.")
	public boolean isValidInput() {
		return !CollectionUtils.isEmpty(positionIds);
	}
}

