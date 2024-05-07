package gohigher.user.entity;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import gohigher.JpaQueryTest;

@DisplayName("RefreshTokenRepository 클래스의")
@JpaQueryTest
class RefreshTokenRepositoryTest {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private TestEntityManager entityManager;
	
	@DisplayName("updateValueByUserId 메서드는")
	@Nested
	class Describe_updateValueByUserId {

		@DisplayName("user_id와 value를 입력받으면")
		@Nested
		class Context_request_with_user_id_and_value {

			Long userId = 1L;

			@BeforeEach
			void setUp() {
				refreshTokenRepository.save(new RefreshTokenJpaEntity(userId, "previousToken"));
				entityManager.clear();
			}

			@DisplayName("새로운 value로 토큰 값을 변경한다")
			@Test
			void it_update_new_token_value() {
				// given
				String newToken = "newToken";

				// when
				refreshTokenRepository.updateValueByUserId(userId, newToken);

				// then
				RefreshTokenJpaEntity refreshTokenJpaEntity = refreshTokenRepository.findByUserId(userId)
					.orElseThrow();
				assertThat(refreshTokenJpaEntity.getValue()).isEqualTo(newToken);
			}
		}
	}

	@DisplayName("findByUserId 메서드는")
	@Nested
	class Describe_findByUserId {

		@DisplayName("userId를 입력받으면")
		@Nested
		class Context_request_with_user_id {

			Long userId = 1L;

			@BeforeEach
			void setUp() {
				refreshTokenRepository.save(new RefreshTokenJpaEntity(userId, "previousToken"));
				entityManager.clear();
			}

			@DisplayName("해당 userId를 가지고 토큰을 찾아 반환한다.")
			@Test
			void it_returns_refresh_token() {
				// given & when
				Optional<RefreshTokenJpaEntity> refreshTokenJpaEntity = refreshTokenRepository.findByUserId(userId);

				// then
				assertThat(refreshTokenJpaEntity).isPresent();
			}
		}
	}

	@DisplayName("deleteByUserId 메서드는")
	@Nested
	class Describe_deleteByUserId {

		@DisplayName("userId를 받으면")
		@Nested
		class Context_request_with_user_id {

			Long userId = 1L;

			@BeforeEach
			void setUp() {
				refreshTokenRepository.save(new RefreshTokenJpaEntity(userId, "previousToken"));
				entityManager.clear();
			}

			@DisplayName("해당 userId를 가지고 토큰을 찾아 반환한다.")
			@Test
			void it_returns_refresh_token() {
				// given & when
				refreshTokenRepository.deleteByUserId(userId);

				// then
				Optional<RefreshTokenJpaEntity> refreshTokenJpaEntity = refreshTokenRepository.findByUserId(userId);
				assertThat(refreshTokenJpaEntity).isEmpty();
			}
		}
	}
}
