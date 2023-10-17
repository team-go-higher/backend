package gohigher.user.port.in;

import java.util.List;

import org.springframework.util.CollectionUtils;

import jakarta.validation.constraints.AssertFalse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DesiredPositionRequest {

	private static final int MAIN_POSITION_IDX = 0;

	private List<Long> positionIds;

	public Long getMainPositionId() {
		return positionIds.get(MAIN_POSITION_IDX);
	}

	@AssertFalse(message = "POSITION_011||희망 직무 ID가 비어있습니다.")
	public boolean isEmptyInput() {
		return CollectionUtils.isEmpty(positionIds);
	}
}
