package gohigher.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

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

@DisplayName("UserPersistenceQueryAdapter 클래스의")
@ExtendWith(MockitoExtension.class)
class UserPersistenceQueryAdapterTest {

	@Mock
	private UserRepository userRepository;

	private UserPersistenceQueryAdapter userPersistenceQueryAdapter;

	@BeforeEach
	void setUp() {
		userPersistenceQueryAdapter = new UserPersistenceQueryAdapter(userRepository);
	}

	@DisplayName("findByEmail 메서드는")
	@Nested
	class findByEmail {

		@DisplayName("입력한 이메일에 해당하는 정보가 있을 때")
		@Nested
		class exist {

			@DisplayName("사용자 정보를 반환해야 한다.")
			@Test
			void success() {
				// given
				String email = "test@email.com";
				UserJpaEntity userJpaEntity = new UserJpaEntity(email, Role.USER, Provider.GOOGLE);
				given(userRepository.findByEmail(email)).willReturn(Optional.of(userJpaEntity));

				// when
				Optional<User> savedUser = userPersistenceQueryAdapter.findByEmail(email);

				// then
				assertThat(savedUser).isPresent();
			}
		}

		@DisplayName("입력한 이메일에 해당하는 정보가 없을 때")
		@Nested
		class notExist {

			@DisplayName("빈 값을 반환해야 한다.")
			@Test
			void success() {
				// given
				String email = "test@email.com";
				given(userRepository.findByEmail(email)).willReturn(Optional.empty());

				// when
				Optional<User> user = userPersistenceQueryAdapter.findByEmail(email);

				// then
				assertThat(user).isEmpty();
			}
		}
	}

	@DisplayName("findById 메서드는")
	@Nested
	class findById {

		@DisplayName("입력한 아이디에 해당하는 정보가 있을 때")
		@Nested
		class exist {

			@DisplayName("사용자 정보를 반환해야 한다.")
			@Test
			void success() {
				// given
				Long id = 1L;
				UserJpaEntity userJpaEntity = new UserJpaEntity("test@email.com", Role.USER, Provider.GOOGLE);
				given(userRepository.findById(id)).willReturn(Optional.of(userJpaEntity));

				// when
				Optional<User> savedUser = userPersistenceQueryAdapter.findById(id);

				// then
				assertThat(savedUser).isPresent();
			}
		}

		@DisplayName("입력한 아이디에 해당하는 정보가 없을 때")
		@Nested
		class notExist {

			@DisplayName("빈 값을 반환해야 한다.")
			@Test
			void success() {
				// given
				Long id = 1L;
				given(userRepository.findById(id)).willReturn(Optional.empty());

				// when
				Optional<User> user = userPersistenceQueryAdapter.findById(id);

				// then
				assertThat(user).isEmpty();
			}
		}
	}
}
