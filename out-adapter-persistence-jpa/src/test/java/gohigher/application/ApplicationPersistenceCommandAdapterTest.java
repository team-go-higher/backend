package gohigher.application;

import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

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
import jakarta.persistence.EntityManager;

@DisplayName("ApplicationPersistenceCommandAdapter 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ApplicationPersistenceCommandAdapterTest {

	private static final Long USER_ID = 1L;
	private final Process firstProcess = TO_APPLY.toDomain();
	private final Process secondProcess = DOCUMENT.toDomain();
	private final Application application = NAVER_APPLICATION.toDomain(firstProcess, secondProcess);

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
				entityManager.clear();

				// then
				ApplicationJpaEntity applicationJpaEntity = applicationRepository.findById(savedApplication.getId())
					.get();
				assertAll(
					() -> assertThat(applicationJpaEntity.getProcesses()).hasSize(application.getProcesses().size()),
					() -> assertThat(applicationJpaEntity.getProcesses()).extracting("type")
						.containsExactly(firstProcess.getType(), secondProcess.getType())
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

			private long applicationId;
			private long secondProcessId;
			private ApplicationProcessJpaEntity firstProcessJpaEntity;
			private ApplicationProcessJpaEntity secondProcessJpaEntity;

			@BeforeEach
			void setUp() {
				ApplicationJpaEntity applicationJpaEntity =
					applicationRepository.save(ApplicationJpaEntity.of(application, USER_ID));
				applicationId = applicationJpaEntity.getId();

				firstProcessJpaEntity = applicationProcessRepository.save(
					ApplicationProcessJpaEntity.of(applicationJpaEntity, firstProcess, true));
				applicationJpaEntity.addProcess(firstProcessJpaEntity);

				secondProcessJpaEntity = applicationProcessRepository.save(
					ApplicationProcessJpaEntity.of(applicationJpaEntity, secondProcess, false));
				applicationJpaEntity.addProcess(secondProcessJpaEntity);

				secondProcessId = secondProcessJpaEntity.getId();
			}

			@DisplayName("정상적으로 변경할 수 있다.")
			@Test
			void updateCurrentProcessOrder() {
				//when
				applicationPersistenceCommandAdapter.updateCurrentProcessOrder(applicationId, USER_ID, secondProcessId);

				//then
				assertAll(
					() -> assertThat(firstProcessJpaEntity.isCurrent()).isFalse(),
					() -> assertThat(secondProcessJpaEntity.isCurrent()).isTrue()
				);
			}
		}
	}

	@DisplayName("updateSimply 메서드는")
	@Nested
	class Describe_updateSimply {

		@DisplayName("지원서 정보와 전형 과정이 변경된 정보일 경우")
		@Nested
		class Context_with_changed_application_and_process {

			private long applicationId;
			private Application applicationToUpdate;
			private Long processId;
			private String companyNameToUpdate = "new Naver";
			private String potisionToUpdate = "new position";
			private String urlToUpdate = "www.update.com";
			private LocalDateTime scheduleToUpdate = LocalDateTime.now().plusDays(10);

			@BeforeEach
			void setUp() {
				ApplicationJpaEntity applicationJpaEntity =
					applicationRepository.save(ApplicationJpaEntity.of(application, USER_ID));
				applicationId = applicationJpaEntity.getId();

				ApplicationProcessJpaEntity firstProcessJpaEntity = applicationProcessRepository.save(
					ApplicationProcessJpaEntity.of(applicationJpaEntity, firstProcess, true));
				applicationJpaEntity.addProcess(firstProcessJpaEntity);

				ApplicationProcessJpaEntity secondProcessJpaEntity = applicationProcessRepository.save(
					ApplicationProcessJpaEntity.of(applicationJpaEntity, secondProcess, false));
				applicationJpaEntity.addProcess(secondProcessJpaEntity);

				processId = secondProcessJpaEntity.getId();

				applicationToUpdate = applicationJpaEntity.toDomain();
				applicationToUpdate.updateSimply(companyNameToUpdate, potisionToUpdate, urlToUpdate, processId,
					scheduleToUpdate);

				entityManager.flush();
				entityManager.clear();
			}

			@DisplayName("정상적으로 값들을 변경한다.")
			@Test
			void it_update_all_data() {
				// when
				applicationPersistenceCommandAdapter.updateSimply(applicationId, processId, applicationToUpdate);
				entityManager.flush();
				entityManager.clear();

				// then
				ApplicationJpaEntity applicationJpaEntity = applicationRepository.findById(applicationId).get();
				assertAll(
					() -> assertThat(applicationJpaEntity.getCompanyName()).isEqualTo(companyNameToUpdate),
					() -> assertThat(applicationJpaEntity.getPosition()).isEqualTo(potisionToUpdate),
					() -> assertThat(applicationJpaEntity.getUrl()).isEqualTo(urlToUpdate),
					() -> assertThat(applicationJpaEntity.getProcesses()).extracting("type", "schedule")
						.contains(
							tuple(firstProcess.getType(), firstProcess.getSchedule()),
							tuple(secondProcess.getType(), scheduleToUpdate)
						)
				);
			}
		}
	}
}
