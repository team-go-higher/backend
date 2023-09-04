package gohigher.user;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@DisplayName("이메일을 이용하여 사용자 조회 테스트")
	@Nested
	class findByEmail {

		@DisplayName("해당하는 이메일이 있을 경우 사용자 정보를 반환한다")
		@Test
		void exist() {
			String email = "test@email.com";
			UserJpaEntity user = new UserJpaEntity(email, Role.USER, Provider.GOOGLE);
			userRepository.save(user);

			Optional<UserJpaEntity> savedUser = userRepository.findByEmail(email);

			assertThat(savedUser).isPresent();
		}

		@DisplayName("해당하는 이메일이 없을 경우 빈 값을 반환한다")
		@Test
		void notExist() {
			String email = "test@email.com";

			Optional<UserJpaEntity> user = userRepository.findByEmail(email);

			assertThat(user).isEmpty();
		}
	}
}
