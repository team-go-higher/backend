package gohigher.application;

import gohigher.global.exception.GoHigherException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VisibleSwitch {

	private boolean isVisible;

	public void convert(boolean isVisible) {
		if (this.isVisible == isVisible) {
			throw new GoHigherException(ApplicationErrorCode.ALREADY_VISIBLE_STATE_TO_CHANGE);
		}

		this.isVisible = isVisible;
	}
}
