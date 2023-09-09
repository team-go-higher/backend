package gohigher.application.port.in;

import static gohigher.application.ApplicationErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import gohigher.global.exception.GoHigherException;

@DisplayName("CalenderApplicationMonthRequest 클래스의")
class CalenderApplicationMonthRequestTest {

	@DisplayName("생성자는")
	@Nested
	class Describe_constuctor {

		@DisplayName("연도 정보가 0보다 작은 수일 경우")
		@Nested
		class Context_with_negative_year_info {

			private final Long userId = 1L;
			private final int month = 9;

			@DisplayName("INVALID_DATE_INFO 에러를 반환한다.")
			@ParameterizedTest
			@ValueSource(ints = {0, -1})
			void it_throw_exception(int year) {
				GoHigherException goHigherException = assertThrows(GoHigherException.class,
					() -> new CalenderApplicationMonthRequest(userId, year, month));
				assertThat(goHigherException.getErrorCode()).isEqualTo(INVALID_DATE_INFO.getErrorCode());
			}
		}

		@DisplayName("1~12 범위에 해당하지 않는 월 정보일 경우")
		@Nested
		class Context_with_invalid_month_info {

			private final Long userId = 1L;
			private final int year = 2023;

			@DisplayName("INVALID_DATE_INFO 에러를 반환한다.")
			@ParameterizedTest
			@ValueSource(ints = {0, 13, 20})
			void it_throw_exception(int month) {
				GoHigherException goHigherException = assertThrows(GoHigherException.class,
					() -> new CalenderApplicationMonthRequest(userId, year, month));
				assertThat(goHigherException.getErrorCode()).isEqualTo(INVALID_DATE_INFO.getErrorCode());
			}
		}
	}
}
