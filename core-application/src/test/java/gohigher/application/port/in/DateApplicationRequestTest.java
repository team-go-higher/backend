package gohigher.application.port.in;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import gohigher.application.ApplicationErrorCode;
import gohigher.global.exception.GlobalErrorCode;
import gohigher.global.exception.GoHigherException;

@DisplayName("DateApplicationMonthRequest 클래스의")
class DateApplicationRequestTest {

	@DisplayName("생정자는")
	@Nested
	class Describe_constructor {

		@DisplayName("형식에 맞는 날짜가 주어진다면")
		@Nested
		class Context_with_right_dateFormat {

			@DisplayName("정상적으로 객체를 생성한다")
			@Test
			void it_construct() {
				// given
				Long userId = 1L;
				String date = "2023-09-13";

				// when & then
				assertThatCode(() -> new DateApplicationRequest(userId, date))
					.doesNotThrowAnyException();
			}
		}
	}

	@DisplayName("형식에 맞지 않는 날짜가 주어진다면")
	@Nested
	class Context_with_wrong_dateFormat {

		@DisplayName("예외를 발생시킨다")
		@ParameterizedTest
		@ValueSource(strings = {"202-12-24", "20232-12-23", "2023-1-23", "2023-09-232", "20230908"})
		void it_throws_exception(String date) {
			// given
			Long userId = 1L;

			// when & then
			assertThatThrownBy(() -> new DateApplicationRequest(userId, date))
				.isInstanceOf(GoHigherException.class)
				.hasMessage(ApplicationErrorCode.INVALID_DATE_PATTERN.getMessage());
		}
	}

	@DisplayName("빈 입력값이 날짜로 주어진다면")
	@Nested
	class Context_with_empty_date {

		@DisplayName("예외를 발생시킨다")
		@ParameterizedTest
		@ValueSource(strings = {"", " ", "	"})
		void it_throws_exception(String date) {
			// given
			Long userId = 1L;

			// when & then
			assertThatThrownBy(() -> new DateApplicationRequest(userId, date))
				.isInstanceOf(GoHigherException.class)
				.hasMessage(GlobalErrorCode.INPUT_EMPTY_ERROR.getMessage());
		}
	}
}
