package gohigher.application;

import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static gohigher.fixtureConverter.ApplicationFixtureConverter.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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

@DisplayName("ApplicationProcessPersistenceQueryAdapter 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Transactional
class ApplicationProcessPersistenceQueryAdapterTest {

	private static final long USER_ID = 1L;

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ApplicationProcessRepository applicationProcessRepository;

	@Autowired
	private ApplicationProcessPersistenceQueryAdapter applicationProcessPersistenceQueryAdapter;

	@DisplayName("findByApplicationIdAndProcessType 메서드는")
	@Nested
	class Describe_FindByApplicationIdAndProcessType {

		@DisplayName("지원서 id와 전형 타입에 따른 지원서의 전형들을 찾으려고 할 때, ")
		@Nested
		class FindByApplicationIdAndProcessType {

			private Long applicationId;

			@BeforeEach
			void setUp() {
				ApplicationJpaEntity applicationJpaEntity = applicationRepository.save(
					convertToApplicationEntity(USER_ID, NAVER_APPLICATION.toDomain()));
				applicationId = applicationJpaEntity.getId();
				ApplicationProcessJpaEntity firstApplicationProcessJpaEntity =
					convertToApplicationProcessEntity(applicationJpaEntity, DOCUMENT.toDomain(), 0);
				ApplicationProcessJpaEntity thirdApplicationProcessJpaEntity = convertToApplicationProcessEntity(
					applicationJpaEntity, ProcessFixture.TEST.toDomainWithDescription("a"), 2);
				ApplicationProcessJpaEntity secondApplicationProcessJpaEntity = convertToApplicationProcessEntity(
					applicationJpaEntity, ProcessFixture.TEST.toDomainWithDescription("b"), 1);
				applicationProcessRepository.saveAll(List.of(firstApplicationProcessJpaEntity,
					secondApplicationProcessJpaEntity, thirdApplicationProcessJpaEntity));
			}

			@DisplayName("정상적으로 찾을 수 있어야 한다.")
			@Test
			void findByApplicationIdAndProcessType() {
				//given
				ProcessType type = ProcessType.TEST;

				//when
				List<Process> actual =
					applicationProcessPersistenceQueryAdapter.findByApplicationIdAndProcessType(applicationId, type);

				//then
				assertAll(
					() -> assertThat(actual).hasSize(2),
					() -> assertThat(actual.get(0).getDescription()).isEqualTo("b"),
					() -> assertThat(actual.get(1).getDescription()).isEqualTo("a")
				);
			}
		}
	}
}