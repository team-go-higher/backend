package gohigher.application;

import gohigher.global.exception.GoHigherException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CompletedSwitch {

	private boolean isCompleted;

	public void convert(boolean isCompleted) {
		if (this.isCompleted == isCompleted) {
			throw new GoHigherException(ApplicationErrorCode.ALREADY_VISIBLE_STATE_TO_CHANGE);
		}

		this.isCompleted = isCompleted;
	}
}
