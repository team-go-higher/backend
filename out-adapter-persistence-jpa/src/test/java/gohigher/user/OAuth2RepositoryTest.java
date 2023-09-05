package gohigher.user;

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

@ExtendWith(MockitoExtension.class)
@DisplayName("OAuth2Repository 클래스의")
class OAuth2RepositoryTest {

	@Mock
	private UserRepository userRepository;

	private OAuth2Repository oAuth2Repository;

	@BeforeEach
	void setUp() {
		oAuth2Repository = new OAuth2Repository(userRepository);
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
				Optional<User> savedUser = oAuth2Repository.findByEmail(email);

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
				Optional<User> user = oAuth2Repository.findByEmail(email);

				// then
				assertThat(user).isEmpty();
			}
		}
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
				UserJpaEntity userJpaEntity = new UserJpaEntity(email, Role.USER, Provider.GOOGLE);
				given(userRepository.save(any())).willReturn(userJpaEntity);

				// when
				User savedUser = oAuth2Repository.save(new User(email, Role.USER, Provider.GOOGLE));

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
