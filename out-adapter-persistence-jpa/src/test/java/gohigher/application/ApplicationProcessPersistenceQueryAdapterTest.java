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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.entity.ApplicationRepository;
import gohigher.common.Process;
import gohigher.common.ProcessType;

@DisplayName("ApplicationProcessPersistenceQueryAdapter 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ApplicationProcessPersistenceQueryAdapterTest {

	private static final long USER_ID = 1L;

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ApplicationProcessRepository applicationProcessRepository;
	
	private ApplicationProcessPersistenceQueryAdapter applicationProcessPersistenceQueryAdapter;

	@BeforeEach
	void setUp() {
		applicationProcessPersistenceQueryAdapter = new ApplicationProcessPersistenceQueryAdapter(
			applicationProcessRepository);
	}

	@DisplayName("findByApplicationIdAndProcessType 메서드는")
	@Nested
	class Describe_FindByApplicationIdAndProcessType {

		@DisplayName("지원서 id와 전형 타입에 따른 지원서의 전형들을 찾으려고 할 때, ")
		@Nested
		class FindByApplicationIdAndProcessType {

			private Long applicationId;

			@BeforeEach
			void setUp() {
				Application application = NAVER_APPLICATION.toDomain(TEST, FIRST_INTERVIEW, SECOND_INTERVIEW);
				ApplicationJpaEntity applicationJpaEntity = applicationRepository.save(
					convertToApplicationEntity(USER_ID, application));

				for (Process process : application.getProcesses()) {
					ApplicationProcessJpaEntity applicationProcessJpaEntity = applicationProcessRepository.save(
						convertToApplicationProcessEntity(applicationJpaEntity, process,
							application.getCurrentProcess()));

					applicationJpaEntity.addProcess(applicationProcessJpaEntity);
				}

				applicationId = applicationJpaEntity.getId();
			}

			@DisplayName("정상적으로 찾을 수 있어야 한다.")
			@Test
			void findByApplicationIdAndProcessType() {
				//given
				ProcessType type = ProcessType.INTERVIEW;

				//when
				List<Process> actual =
					applicationProcessPersistenceQueryAdapter.findByApplicationIdAndProcessType(applicationId, type);

				//then
				assertAll(
					() -> assertThat(actual).hasSize(2),
					() -> assertThat(actual.get(0).getDescription()).isEqualTo(FIRST_INTERVIEW.getDescription()),
					() -> assertThat(actual.get(1).getDescription()).isEqualTo(SECOND_INTERVIEW.getDescription())
				);
			}
		}
	}
}
