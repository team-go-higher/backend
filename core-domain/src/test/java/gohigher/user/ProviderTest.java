package gohigher.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import gohigher.global.exception.GoHigherException;

@DisplayName("Provider 클래스의")
public class ProviderTest {

	@DisplayName("from 메서드는")
	@Nested
	class from {

		@DisplayName("문자열 google 이 입력될 때")
		@Nested
		class google {

			@DisplayName("google provider 를 반환해야 한다.")
			@ParameterizedTest
			@ValueSource(strings = {"google", "Google", "GOOGLE"})
			void success(String provider) {
				assertThat(Provider.from(provider)).isEqualTo(Provider.GOOGLE);
			}
		}

		@DisplayName("문자열 kakao 가 입력될 때")
		@Nested
		class kakao {

			@DisplayName("kakao provider 를 반환해야 한다.")
			@ParameterizedTest
			@ValueSource(strings = {"kakao", "Kakao", "KAKAO"})
			void success(String provider) {
				assertThat(Provider.from(provider)).isEqualTo(Provider.KAKAO);
			}
		}

		@DisplayName("존재하지 않는 provider 가 입력될 때")
		@Nested
		class invalid {

			@DisplayName("예외가 발생해야 한다.")
			@Test
			void fail() {
				assertThatThrownBy(() -> Provider.from("invalid"))
					.isInstanceOf(GoHigherException.class);
			}
		}
	}
}
