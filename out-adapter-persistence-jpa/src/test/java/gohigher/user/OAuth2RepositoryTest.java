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
class OAuth2RepositoryTest {

	@Mock
	private UserRepository userRepository;

	private OAuth2Repository oAuth2Repository;

	@BeforeEach
	void setUp() {
		oAuth2Repository = new OAuth2Repository(userRepository);
	}

	@DisplayName("이메일을 이용하여 사용자 정보 조회")
	@Nested
	class findByEmail {

		@DisplayName("해당하는 이메일이 있을 경우 사용자 정보를 반환한다")
		@Test
		void exist() {
			String email = "test@email.com";
			UserJpaEntity userJpaEntity = new UserJpaEntity(email, Role.USER, Provider.GOOGLE);
			given(userRepository.findByEmail(email)).willReturn(Optional.of(userJpaEntity));

			Optional<User> savedUser = oAuth2Repository.findByEmail(email);

			assertThat(savedUser).isPresent();
		}

		@DisplayName("해당하는 이메일이 없을 경우 빈 값을 반환한다")
		@Test
		void notExist() {
			String email = "test@email.com";
			given(userRepository.findByEmail(email)).willReturn(Optional.empty());

			Optional<User> user = oAuth2Repository.findByEmail(email);

			assertThat(user).isEmpty();
		}
	}

	@DisplayName("사용자 정보 저장")
	@Nested
	class save {

		@DisplayName("사용자 정보를 저장한다")
		@Test
		void success() {
			String email = "test@email.com";
			UserJpaEntity userJpaEntity = new UserJpaEntity(email, Role.USER, Provider.GOOGLE);
			given(userRepository.save(any())).willReturn(userJpaEntity);

			User savedUser = oAuth2Repository.save(new User(email, Role.USER, Provider.GOOGLE));

			assertAll(
				() -> assertThat(savedUser.getEmail()).isEqualTo(userJpaEntity.getEmail()),
				() -> assertThat(savedUser.getRole()).isEqualTo(userJpaEntity.getRole()),
				() -> assertThat(savedUser.getProvider()).isEqualTo(userJpaEntity.getProvider())
			);
		}
	}
}
