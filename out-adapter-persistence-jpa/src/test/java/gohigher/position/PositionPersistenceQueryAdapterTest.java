package gohigher.position;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import gohigher.position.entity.PositionJpaEntity;
import gohigher.position.entity.PositionRepository;
import jakarta.persistence.EntityManager;

@DisplayName("PositionPersistenceQueryAdapter 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class PositionPersistenceQueryAdapterTest {

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private EntityManager entityManager;

	private PositionPersistenceQueryAdapter positionPersistenceQueryAdapter;

	@BeforeEach
	void setUp() {
		positionPersistenceQueryAdapter = new PositionPersistenceQueryAdapter(positionRepository);
	}

	@DisplayName("existsByValues 메서드는")
	@Nested
	class Describe_existsByValues {

		Position developer = PositionFixture.DEVELOPER.toDomain();
		Position designer = PositionFixture.DESIGNER.toDomain();

		@DisplayName("position들을 입력받았을 때,")
		@Nested
		class Context_input_position {

			List<Long> savedPositionIds;

			@BeforeEach
			void setUp() {
				PositionJpaEntity designerEntity = new PositionJpaEntity(designer.getValue());
				PositionJpaEntity developerEntity = new PositionJpaEntity(developer.getValue());
				List<PositionJpaEntity> savedPositionJpaEntities = positionRepository.saveAll(
					List.of(developerEntity, designerEntity));
				savedPositionIds = savedPositionJpaEntities.stream()
					.map(PositionJpaEntity::getId)
					.collect(Collectors.toList());
				entityManager.clear();
			}

			@DisplayName("동일한 position이 이미 있다면 true를 리턴한다.")
			@Test
			void it_returns_true_when_existed_position() {
				// when & then
				assertThat(positionPersistenceQueryAdapter.existsByIds(savedPositionIds))
					.isTrue();
			}

			@DisplayName("동일한 position이 없다면 false를 리턴한다.")
			@Test
			void it_returns_false_when_not_existed_position() {
				// when & then
				assertThat(positionPersistenceQueryAdapter.existsByIds(List.of(0L)))
					.isFalse();
			}
		}
	}
}
