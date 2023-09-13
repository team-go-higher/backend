package gohigher.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import gohigher.application.entity.ApplicationProcessJpaEntity;
import gohigher.application.entity.ApplicationProcessRepository;
import gohigher.application.entity.ApplicationRepository;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import gohigher.common.ProcessType;
import jakarta.persistence.EntityManager;

@DisplayName("ApplicationPersistenceCommandAdapter 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Transactional
class ApplicationPersistenceCommandAdapterTest {

	@Autowired
	private ApplicationRepository applicationRepository;
	@Autowired
	private ApplicationProcessRepository applicationProcessRepository;
	@Autowired
	private ApplicationPersistenceCommandAdapter applicationPersistenceCommandAdapter;
	@Autowired
	private EntityManager entityManager;

	private final Process firstProcess = new Process(ProcessType.TEST, "코딩테스트", LocalDateTime.now());
	private final Process secondProcess = new Process(ProcessType.INTERVIEW, "기술 면접", LocalDateTime.now());
	private final Process thirdProcess = new Process(ProcessType.INTERVIEW, "인성 면접", LocalDateTime.now());
	Application application = new Application(null, "", "", "", "", "", "", "", "",
		EmploymentType.PERMANENT, "", "", "", LocalDateTime.now(),
		List.of(firstProcess, secondProcess, thirdProcess), "", 1L, firstProcess);

	@DisplayName("save 메서드는")
	@Nested
	class Describe_Save {

		@DisplayName("전형 단계들을 포함한 지원서를 저장하려고 할 때,")
		@Nested
		class UpdateCurrentProcessOrderWithId {

			@DisplayName("정상적으로 저장할 수 있다.")
			@Test
			void updateCurrentProcessOrder() {
				//when
				applicationPersistenceCommandAdapter.save(application);

				//then
				List<ApplicationProcessJpaEntity> applicationProcesses = applicationProcessRepository.findAll();
				assertAll(
					() -> assertThat(applicationProcesses).hasSize(3),
					() -> assertThat(applicationProcesses).extracting("order", "description")
						.contains(
							tuple(0, "코딩테스트"),
							tuple(1, "기술 면접"),
							tuple(2, "인성 면접"))
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

			@BeforeEach
			void setUp() {
				applicationId = applicationPersistenceCommandAdapter.save(application);
				entityManager.clear();
			}

			@DisplayName("정상적으로 변경할 수 있다.")
			@Test
			void updateCurrentProcessOrder() {
				//given
				int currentProcessOrder = 2;

				//when
				applicationPersistenceCommandAdapter.updateCurrentProcessOrder(applicationId, currentProcessOrder);

				//then
				entityManager.clear();
				int currentProcessOrderAfterUpdate = applicationRepository.findById(applicationId)
					.get()
					.getCurrentProcessOrder();
				assertThat(currentProcessOrderAfterUpdate).isEqualTo(currentProcessOrder);
			}
		}
	}
}
