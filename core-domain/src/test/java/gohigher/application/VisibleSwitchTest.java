package gohigher.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gohigher.global.exception.GoHigherException;

class VisibleSwitchTest {

	@DisplayName("스위치의 상태를 변경한다.")
	@Test
	void convert() {
		// given
		VisibleSwitch visibleSwitch = new VisibleSwitch(true);

		// when
		visibleSwitch.convert(false);

		// then
		assertThat(visibleSwitch.isVisible()).isFalse();
	}

	@DisplayName("스위치의 상태를 현재 상태와 동일하게 변경할 시 예외를 발생한다.")
	@Test
	void throw_exception_when_convert_to_same_state() {
		// given
		boolean state = true;
		VisibleSwitch visibleSwitch = new VisibleSwitch(state);

		// when & then
		assertThatThrownBy(() -> visibleSwitch.convert(state)).isInstanceOf(GoHigherException.class)
			.hasMessage(ApplicationErrorCode.ALREADY_VISIBLE_STATE_TO_CHANGE.getMessage());
	}
}
