package gohigher.user;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import gohigher.user.auth.Provider;
import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;

@DisplayName("UserRepository 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

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
				UserJpaEntity user = new UserJpaEntity(email, Role.USER, Provider.GOOGLE);
				userRepository.save(user);

				// when
				Optional<UserJpaEntity> savedUser = userRepository.findByEmail(email);

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

				// when
				Optional<UserJpaEntity> user = userRepository.findByEmail(email);

				// then
				assertThat(user).isEmpty();
			}
		}
	}
}
