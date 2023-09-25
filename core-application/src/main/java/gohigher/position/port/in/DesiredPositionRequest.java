package gohigher.position.port.in;

import java.util.List;

import org.springframework.util.CollectionUtils;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DesiredPositionRequest {

	private List<Long> existedPositionIds;
	private List<String> personalPositions;

	@AssertTrue(message = "GLOBAL_011||빈 입력값입니다.")
	public boolean isValidInput() {
		return !CollectionUtils.isEmpty(existedPositionIds) || !CollectionUtils.isEmpty(personalPositions);
	}

	public boolean isExistedPositionIdsEmpty() {
		return CollectionUtils.isEmpty(existedPositionIds);
	}

	public boolean isPersonalPositionsEmpty() {
		return CollectionUtils.isEmpty(personalPositions);
	}
}

