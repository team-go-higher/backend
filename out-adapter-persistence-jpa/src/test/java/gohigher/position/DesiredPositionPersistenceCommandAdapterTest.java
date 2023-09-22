package gohigher.position;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import gohigher.fixtureConverter.PositionFixtureConverter;
import gohigher.fixtureConverter.UserFixtureConvertor;
import gohigher.position.entity.DesiredPositionRepository;
import gohigher.position.entity.PositionJpaEntity;
import gohigher.position.entity.PositionRepository;
import gohigher.user.UserFixture;
import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;
import jakarta.persistence.EntityManager;

@DisplayName("DesiredPositionPersistenceCommandAdapter 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class DesiredPositionPersistenceCommandAdapterTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private DesiredPositionRepository desiredPositionRepository;

	@Autowired
	private EntityManager entityManager;

	private DesiredPositionPersistenceCommandAdapter desiredPositionPersistenceCommandAdapter;

	@BeforeEach
	void setUp() {
		desiredPositionPersistenceCommandAdapter = new DesiredPositionPersistenceCommandAdapter(
			userRepository, positionRepository, desiredPositionRepository);
	}

	@DisplayName("saveDesiredPositions 메서드는")
	@Nested
	class Describe_saveDesiredPositions {

		UserJpaEntity azpi = UserFixtureConvertor.convertToUserEntity(UserFixture.AZPI.toDomain());
		PositionJpaEntity developer = PositionFixtureConverter.convertToPositionEntity(
			PositionFixture.DEVELOPER.toDomain());
		PositionJpaEntity designer = PositionFixtureConverter.convertToPositionEntity(
			PositionFixture.DESIGNER.toDomain());

		@DisplayName("n개의 positionId를 입력받으면")
		@Nested
		class Context_input_positionIds {

			Long userId;
			List<Long> positionIds;

			@BeforeEach
			void setUp() {
				UserJpaEntity savedAzpi = userRepository.save(azpi);
				List<PositionJpaEntity> savedPosition = positionRepository.saveAll(List.of(developer, designer));
				userId = savedAzpi.getId();
				positionIds = savedPosition.stream()
					.map(PositionJpaEntity::getId)
					.toList();
				entityManager.clear();
			}

			@DisplayName("해당 id의 position들을 user의 희망직무로 저장한다.")
			@Test
			void it_saves_user_desired_positions() {
				// given & when & then
				assertThatNoException().isThrownBy(
					() -> desiredPositionPersistenceCommandAdapter.saveDesiredPositions(userId, positionIds));
			}
		}
	}
}
