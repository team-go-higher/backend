package gohigher.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import gohigher.global.exception.GoHigherException;

public class ProviderTest {

	@DisplayName("문자열을 이용하여 적절한 provider 조회")
	@Nested
	class from {

		@DisplayName("google provider 를 조회한다")
		@ParameterizedTest
		@ValueSource(strings = {"google", "Google", "GOOGLE"})
		void google(String provider) {
			assertThat(Provider.from(provider)).isEqualTo(Provider.GOOGLE);
		}

		@DisplayName("kakao provider 를 조회한다")
		@ParameterizedTest
		@ValueSource(strings = {"kakao", "Kakao", "KAKAO"})
		void kakao(String provider) {
			assertThat(Provider.from(provider)).isEqualTo(Provider.KAKAO);
		}

		@DisplayName("존재하지 않는 provider 요청 시 예외가 발생한다")
		@Test
		void invalidProvider() {
			assertThatThrownBy(() -> Provider.from("invalid"))
				.isInstanceOf(GoHigherException.class);
		}
	}
}
