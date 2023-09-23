package gohigher.position.port.in;

import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@DesiredPositionBlank(existedPositionIds = "existedPositionIds", personalPositions = "personalPositions")
public class DesiredPositionRequest {

	private List<Long> existedPositionIds;
	private List<String> personalPositions;

	public boolean isExistedPositionIdsEmpty() {
		return CollectionUtils.isEmpty(existedPositionIds);
	}

	public boolean isPersonalPositionsEmpty() {
		return CollectionUtils.isEmpty(personalPositions);
	}
}

