package gohigher.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import gohigher.JpaQueryTest;
import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.entity.ApplicationRepository;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import gohigher.user.UserFixture;
import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;
import jakarta.persistence.EntityManager;

@DisplayName("ApplicationProcessPersistenceCommandAdapter 클래스의")
@JpaQueryTest
class ApplicationProcessPersistenceCommandAdapterTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ApplicationProcessRepository applicationProcessRepository;

	@Autowired
	private EntityManager entityManager;

	private ApplicationProcessPersistenceCommandAdapter applicationProcessPersistenceCommandAdapter;

	private Long userId;

	@BeforeEach
	void setUp() {
		applicationProcessPersistenceCommandAdapter = new ApplicationProcessPersistenceCommandAdapter(
			applicationRepository, applicationProcessRepository);
		UserJpaEntity savedUser = userRepository.save(UserJpaEntity.from(UserFixture.AZPI.toDomain()));
		userId = savedUser.getId();
	}

	@DisplayName("save 메서드는")
	@Nested
	class Describe_save {

		private Long applicationId;

		@BeforeEach
		void setUp() {
			Application kakao = ApplicationFixture.KAKAO_APPLICATION.toDomain(ProcessFixture.TEST);
			ApplicationJpaEntity savedApplication = applicationRepository.save(ApplicationJpaEntity.of(kakao, userId));
			applicationId = savedApplication.getId();
			entityManager.clear();
		}

		@DisplayName("application_id와 Process 객체를 받으면")
		@Nested
		class Context_input_applicationId_and_Process {

			@DisplayName("해당 정보를 저장하고 값을 리턴한다.")
			@Test
			void it_returns_saved_value() {
				// given
				Process newProcess = Process.makeFirstByType(ProcessType.INTERVIEW, "1차 면접");

				// when
				Process savedProcess = applicationProcessPersistenceCommandAdapter.save(applicationId, newProcess);

				// then
				assertAll(
					() -> assertThat(savedProcess.getDescription()).isEqualTo(newProcess.getDescription()),
					() -> assertThat(savedProcess.getId()).isNotNull()
				);
			}
		}
	}
}
