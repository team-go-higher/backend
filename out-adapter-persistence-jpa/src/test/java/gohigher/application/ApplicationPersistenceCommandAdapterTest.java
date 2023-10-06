package gohigher.application;

import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
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
import jakarta.persistence.EntityManager;

@DisplayName("ApplicationPersistenceCommandAdapter 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ApplicationPersistenceCommandAdapterTest {

	private static final String FIRST_PROCESS_DESCRIPTION = "코딩테스트";
	private static final String SECOND_PROCESS_DESCRIPTION = "기술면접";
	private static final Long USER_ID = 1L;
	private final Process firstProcess = TO_APPLY.toDomainWithDescription(FIRST_PROCESS_DESCRIPTION);
	private final Process secondProcess = DOCUMENT.toDomainWithDescription(SECOND_PROCESS_DESCRIPTION);
	private final Application application = NAVER_APPLICATION.toDomain(List.of(firstProcess, secondProcess),
		firstProcess);

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ApplicationProcessRepository applicationProcessRepository;

	private ApplicationPersistenceCommandAdapter applicationPersistenceCommandAdapter;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	void setUp() {
		applicationPersistenceCommandAdapter =
			new ApplicationPersistenceCommandAdapter(applicationRepository, applicationProcessRepository);
	}

	@DisplayName("save 메서드는")
	@Nested
	class Describe_Save {

		@DisplayName("전형 단계들을 포함한 지원서를 저장하려고 할 때")
		@Nested
		class Context_save_with_processes {

			@DisplayName("정상적으로 저장할 수 있다")
			@Test
			void it_save_application_with_processes() {
				// when
				Application savedApplication = applicationPersistenceCommandAdapter.save(USER_ID, application);

				// then
				assertAll(
					() -> assertThat(savedApplication.getProcesses()).hasSize(application.getProcesses().size()),
					() -> assertThat(savedApplication.getProcesses()).extracting("type")
						.containsExactly(ProcessType.TO_APPLY, ProcessType.DOCUMENT)
				);
			}
		}
	}

	@DisplayName("updateCurrentProcessOrder 메서드는")
	@Nested
	class Describe_UpdateCurrentProcessOrder {

		@DisplayName("application의 id에 해당하는 데이터의 order를 변경하려고 할 때")
		@Nested
		class UpdateCurrentProcessOrderWithId {

			long applicationId;
			long applicationProcessId;
			long expectedProcessOrder;

			@BeforeEach
			void setUp() {
				ApplicationJpaEntity applicationJpaEntity =
					applicationRepository.save(ApplicationJpaEntity.of(application, USER_ID));
				applicationId = applicationJpaEntity.getId();
				ApplicationProcessJpaEntity secondProcessJpaEntity = applicationProcessRepository.save(
					ApplicationProcessJpaEntity.of(applicationJpaEntity, secondProcess, 1));
				applicationProcessId = secondProcessJpaEntity.getId();
				expectedProcessOrder = secondProcessJpaEntity.getOrder();
			}

			@DisplayName("정상적으로 변경할 수 있다.")
			@Test
			void updateCurrentProcessOrder() {
				//when
				applicationPersistenceCommandAdapter.updateCurrentProcessOrder(applicationId, applicationProcessId);

				//then
				entityManager.clear();
				int currentProcessOrder = applicationRepository.findById(applicationId)
					.get()
					.getCurrentProcessOrder();
				assertThat(currentProcessOrder).isEqualTo(expectedProcessOrder);
			}
		}
	}
}
