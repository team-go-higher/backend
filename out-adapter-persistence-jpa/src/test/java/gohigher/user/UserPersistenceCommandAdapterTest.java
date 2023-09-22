package gohigher.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.user.auth.Provider;
import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;

@DisplayName("UserPersistenceCommandAdapter 클래스의")
@ExtendWith(MockitoExtension.class)
class UserPersistenceCommandAdapterTest {

	@Mock
	private UserRepository userRepository;

	private UserPersistenceCommandAdapter userPersistenceCommandAdapter;

	@BeforeEach
	void setUp() {
		userPersistenceCommandAdapter = new UserPersistenceCommandAdapter(userRepository);
	}

	@DisplayName("save 메서드는")
	@Nested
	class save {

		@DisplayName("사용자 정보가 입력될 때")
		@Nested
		class insert {

			@DisplayName("입력받은 사용자 정보를 저장해야 한다.")
			@Test
			void success() {
				// given
				String email = "test@email.com";
				UserJpaEntity userJpaEntity = new UserJpaEntity(email, Role.GUEST, Provider.GOOGLE);
				given(userRepository.save(any())).willReturn(userJpaEntity);

				// when
				User savedUser = userPersistenceCommandAdapter.save(User.joinAsGuest(email, Provider.GOOGLE));

				// then
				assertAll(
					() -> assertThat(savedUser.getEmail()).isEqualTo(userJpaEntity.getEmail()),
					() -> assertThat(savedUser.getRole()).isEqualTo(userJpaEntity.getRole()),
					() -> assertThat(savedUser.getProvider()).isEqualTo(userJpaEntity.getProvider())
				);
			}
		}
	}
}
