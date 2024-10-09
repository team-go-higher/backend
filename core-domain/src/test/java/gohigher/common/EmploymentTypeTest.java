package gohigher.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmploymentTypeTest {

	@DisplayName("UNDEFINED 인 경우 null값 반환")
	@Test
	void getValue_if_UNDEFINED() {
		// given
		EmploymentType employmentType = EmploymentType.UNDEFINED;

		// when & then
		assertThat(employmentType.getValue()).isNull();
	}

	@DisplayName("null이 들어오는경우 UNDEFINED 반환")
	@Test
	void makeEmploymentByNull() {
		// given & when & then
		assertThat(EmploymentType.from(null)).isEqualTo(EmploymentType.UNDEFINED);
	}
}
