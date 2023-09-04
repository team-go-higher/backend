package gohigher.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.port.out.OAuth2PersistenceCommandPort;
import gohigher.port.out.OAuth2PersistenceQueryPort;
import gohigher.user.Provider;
import gohigher.user.Role;
import gohigher.user.User;

@ExtendWith(MockitoExtension.class)
class OAuth2CommandServiceTest {

	@Mock
	private OAuth2PersistenceQueryPort oAuth2PersistenceQueryPort;

	@Mock
	private OAuth2PersistenceCommandPort oAuth2PersistenceCommandPort;

	private OAuth2CommandService oAuth2CommandService;

	@BeforeEach
	void setUp() {
		oAuth2CommandService = new OAuth2CommandService(oAuth2PersistenceQueryPort, oAuth2PersistenceCommandPort);
	}

	@DisplayName("로그인 테스트")
	@Nested
	class login {

		@DisplayName("가입된 사용자일 경우 저장되어 있는 정보를 반환한다")
		@Test
		void joined() {
			String email = "test@gmail.com";
			User user = new User(email, Role.USER, Provider.GOOGLE);
			given(oAuth2PersistenceQueryPort.findByEmail(email)).willReturn(Optional.of(user));

			User savedUser = oAuth2CommandService.login(email, Provider.GOOGLE);

			assertThat(savedUser).isEqualTo(user);
		}

		@DisplayName("가입하지 않았던 사용자일 경우 사용자 정보를 저장한다")
		@Test
		void join() {
			String email = "test@gmail.com";
			Provider provider = Provider.GOOGLE;
			User user = new User(email, Role.GUEST, Provider.GOOGLE);

			given(oAuth2PersistenceQueryPort.findByEmail(email)).willReturn(Optional.empty());
			given(oAuth2PersistenceCommandPort.save(any())).willReturn(user);

			User savedUser = oAuth2CommandService.login(email, provider);

			assertAll(
				() -> assertThat(savedUser.getEmail()).isEqualTo(user.getEmail()),
				() -> assertThat(savedUser.getRole()).isEqualTo(user.getRole()),
				() -> assertThat(savedUser.getProvider()).isEqualTo(user.getProvider())
			);
		}
	}
}
