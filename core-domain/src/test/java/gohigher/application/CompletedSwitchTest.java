package gohigher.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gohigher.global.exception.GoHigherException;

class CompletedSwitchTest {

	@DisplayName("스위치의 상태를 변경한다.")
	@Test
	void convert() {
		// given
		CompletedSwitch completedSwitch = new CompletedSwitch(true);

		// when
		completedSwitch.convert(false);

		// then
		assertThat(completedSwitch.isCompleted()).isFalse();
	}

	@DisplayName("스위치의 상태를 현재 상태와 동일하게 변경할 시 예외를 발생한다.")
	@Test
	void throw_exception_when_convert_to_same_state() {
		// given
		boolean state = true;
		CompletedSwitch completedSwitch = new CompletedSwitch(state);

		// when & then
		assertThatThrownBy(() -> completedSwitch.convert(state)).isInstanceOf(GoHigherException.class)
			.hasMessage(ApplicationErrorCode.ALREADY_VISIBLE_STATE_TO_CHANGE.getMessage());
	}
}
