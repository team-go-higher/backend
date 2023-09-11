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

import gohigher.user.Role;
import gohigher.user.User;
import gohigher.user.auth.Provider;
import gohigher.user.port.out.UserPersistenceCommandPort;
import gohigher.user.port.out.UserPersistenceQueryPort;
import gohigher.user.usecase.UserCommandService;

@DisplayName("OAuth2CommandService 클래스의")
@ExtendWith(MockitoExtension.class)
class OAuth2CommandServiceTest {

	@Mock
	private UserPersistenceQueryPort userPersistenceQueryPort;

	@Mock
	private UserPersistenceCommandPort userPersistenceCommandPort;

	private UserCommandService userCommandService;

	@BeforeEach
	void setUp() {
		userCommandService = new UserCommandService(userPersistenceQueryPort, userPersistenceCommandPort);
	}

	@DisplayName("login 메서드는")
	@Nested
	class login {

		@DisplayName("해당 이메일로 가입한 사용자가 있을 때")
		@Nested
		class exist {

			@DisplayName("저장되어 있는 정보를 반환해야 한다.")
			@Test
			void success() {
				// given
				String email = "test@gmail.com";
				User user = new User(email, Role.USER, Provider.GOOGLE);
				given(userPersistenceQueryPort.findByEmail(email)).willReturn(Optional.of(user));

				// when
				User savedUser = userCommandService.login(email, Provider.GOOGLE);

				// then
				assertThat(savedUser).isEqualTo(user);
			}
		}

		@DisplayName("해당 이메일로 가입한 사용자가 없을 때")
		@Nested
		class notExist {

			@DisplayName("사용자 정보를 저장해야 한다.")
			@Test
			void success() {
				// given
				String email = "test@gmail.com";
				Provider provider = Provider.GOOGLE;
				User user = new User(email, Role.GUEST, Provider.GOOGLE);

				given(userPersistenceQueryPort.findByEmail(email)).willReturn(Optional.empty());
				given(userPersistenceCommandPort.save(any())).willReturn(user);

				// when
				User savedUser = userCommandService.login(email, provider);

				// then
				assertAll(
					() -> assertThat(savedUser.getEmail()).isEqualTo(user.getEmail()),
					() -> assertThat(savedUser.getRole()).isEqualTo(user.getRole()),
					() -> assertThat(savedUser.getProvider()).isEqualTo(user.getProvider())
				);
			}
		}
	}
}
