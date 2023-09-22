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

import gohigher.position.entity.PositionRepository;
import jakarta.persistence.EntityManager;

@DisplayName("PositionPersistenceCommandAdapter 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class PositionPersistenceCommandAdapterTest {

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private EntityManager entityManager;

	private PositionPersistenceCommandAdapter positionPersistenceCommandAdapter;

	@BeforeEach
	void setUp() {
		positionPersistenceCommandAdapter = new PositionPersistenceCommandAdapter(positionRepository);
	}

	@DisplayName("saveAll 메서드는")
	@Nested
	class Describe_saveAll {

		@DisplayName("position을 n개 입력받는 경우,")
		@Nested
		class Context_input_position_list {

			Position developer = PositionFixture.DEVELOPER.toDomain();
			Position designer = PositionFixture.DESIGNER.toDomain();

			@DisplayName("n개를 저장한 후 id들을 보내준다,")
			@Test
			void it_returns_saved_id_list() {
				// given
				List<Position> positions = List.of(this.designer, developer);

				// when
				List<Long> personalPositionIds = positionPersistenceCommandAdapter.saveAll(positions);

				// then
				assertThat(personalPositionIds).hasSize(positions.size());
			}
		}
	}
}
