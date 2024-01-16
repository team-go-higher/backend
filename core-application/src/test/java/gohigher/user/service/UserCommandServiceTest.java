package gohigher.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.user.User;
import gohigher.user.auth.Provider;
import gohigher.user.port.in.DesiredPositionCommandPort;
import gohigher.user.port.out.UserPersistenceCommandPort;
import gohigher.user.port.out.UserPersistenceQueryPort;

@DisplayName("UserCommandService 클래스의")
@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

	@Mock
	private UserPersistenceQueryPort userPersistenceQueryPort;

	@Mock
	private UserPersistenceCommandPort userPersistenceCommandPort;

	@Mock
	private DesiredPositionCommandPort desiredPositionCommandPort;

	@InjectMocks
	private UserCommandService userCommandService;

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
				User user = User.joinAsGuest(email, Provider.GOOGLE);
				given(userPersistenceQueryPort.findByEmail(email)).willReturn(Optional.of(user));

				// when
				User savedUser = userCommandService.signIn(email, Provider.GOOGLE);

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
				User user = User.joinAsGuest(email, Provider.GOOGLE);

				given(userPersistenceQueryPort.findByEmail(email)).willReturn(Optional.empty());
				given(userPersistenceCommandPort.save(any())).willReturn(user);

				// when
				User savedUser = userCommandService.signIn(email, provider);

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
