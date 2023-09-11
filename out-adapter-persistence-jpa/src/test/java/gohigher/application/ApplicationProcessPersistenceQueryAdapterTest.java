package gohigher.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.entity.ApplicationRepository;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import jakarta.persistence.EntityManager;

@DisplayName("ApplicationProcessPersistenceQueryAdapter 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Transactional
class ApplicationProcessPersistenceQueryAdapterTest {

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ApplicationProcessRepository applicationProcessRepository;

	@Autowired
	private ApplicationProcessPersistenceQueryAdapter applicationProcessPersistenceQueryAdapter;

	@Autowired
	private EntityManager entityManager;

	@DisplayName("findByIdAndApplicationId 메서드는")
	@Nested
	class Describe_FindByIdAndApplicationId {

		@DisplayName("존재하는 id와 지원서 id에 해당하는 지원서의 전형을 찾으려 할 때,")
		@Nested
		class FindApplicationProcessByExistenceIdAndApplicationId {

			private Long applicationId;
			private Long applicationProcessId;

			@BeforeEach
			void setUp() {
				ApplicationJpaEntity applicationJpaEntity = new ApplicationJpaEntity(null, null, null,
					null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, false);
				applicationId = applicationRepository.save(applicationJpaEntity).getId();
				ApplicationProcessJpaEntity applicationProcessJpaEntity = new ApplicationProcessJpaEntity(
					applicationProcessId, applicationJpaEntity, ProcessType.TEST, 0, LocalDateTime.now(), "인성면접");
				applicationProcessId = applicationProcessRepository.save(applicationProcessJpaEntity).getId();
			}

			@DisplayName("지원서의 전형을 반환해야한다.")
			@Test
			void findByIdAndApplicationId() {
				//when
				Optional<Process> actual = applicationProcessPersistenceQueryAdapter.findByIdAndApplicationId(
					applicationProcessId, applicationId);

				//then
				assertAll(
					() -> assertThat(actual).isPresent(),
					() -> assertThat(actual.get().getId()).isEqualTo(applicationProcessId)
				);
			}
		}
	}

}