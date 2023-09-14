package gohigher.application.entity;

import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static gohigher.fixtureConverter.ApplicationFixtureConverter.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DisplayName("ApplicationRepository 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ApplicationRepositoryTest {

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ApplicationProcessRepository applicationProcessRepository;

	@Autowired
	private TestEntityManager entityManager;

	@DisplayName("findByIdAndUserIdWithProcess 메소드는")
	@Nested
	class Describe_findByIdAndUserIdWithProcess {

		private final Long userId = 0L;

		private ApplicationJpaEntity naverApplication;

		@BeforeEach
		void setUp() {
			naverApplication = applicationRepository.save(
				convertToApplicationEntity(userId, NAVER_APPLICATION.toDomain()));
		}

		@DisplayName("등록된 전형 과정이 없을 경우에도")
		@Nested
		class Context_with_no_process {

			@DisplayName("지원서를 반환한다.")
			@Test
			void it_returns_optional_empty() {
				Optional<ApplicationJpaEntity> response = applicationRepository.findByIdAndUserIdWithProcess(
					naverApplication.getId(), userId);

				assertThat(response).isNotEmpty();
			}
		}

		@DisplayName("지원서가 여러 프로세스를 갖고 있을 경우")
		@Nested
		class Context_with_many_processes {

			private List<ApplicationProcessJpaEntity> naverProcesses;

			@BeforeEach
			void setUp() {
				ApplicationProcessJpaEntity toApply = applicationProcessRepository.save(
					convertToApplicationProcessEntity(naverApplication, TO_APPLY.toDomain(), 1));

				ApplicationProcessJpaEntity test = applicationProcessRepository.save(
					convertToApplicationProcessEntity(naverApplication, TEST.toDomain(), 2));

				ApplicationProcessJpaEntity interview = applicationProcessRepository.save(
					convertToApplicationProcessEntity(naverApplication, INTERVIEW.toDomain(), 3));

				naverProcesses = List.of(toApply, test, interview);

				entityManager.clear();
			}

			@DisplayName("모든 과정 데이터와 함께 지원서 데이터를 가져온다.")
			@Test
			void it_returns_application_with_processes() {
				Optional<ApplicationJpaEntity> response = applicationRepository.findByIdAndUserIdWithProcess(
					naverApplication.getId(), userId);

				assertAll(() -> assertThat(response).isNotEmpty(),
					() -> assertThat(response.get().getProcesses()).hasSize(naverProcesses.size()));
			}
		}

		@DisplayName("지원서가 삭제된 경우")
		@Nested
		class Context_with_deleted_application {

			@BeforeEach
			void setUp() {
				naverApplication.changeToDelete();
			}

			@DisplayName("비어있는 결과를 반환한다.")
			@Test
			void it_returns_optional_empty() {
				Optional<ApplicationJpaEntity> response = applicationRepository.findByIdAndUserIdWithProcess(
					naverApplication.getId(), userId);

				assertThat(response).isEmpty();
			}
		}
	}

	@DisplayName("findByUserIdAndMonth 메소드는")
	@Nested
	class Describe_findByUserIdAndMonth {

		private final Long userId = 0L;
		private final int year = 2023;
		private final int month = 9;

		private ApplicationJpaEntity naverApplication;
		private ApplicationJpaEntity kakaoApplication;

		@BeforeEach
		void setUp() {
			naverApplication = applicationRepository.save(
				convertToApplicationEntity(userId, NAVER_APPLICATION.toDomain()));
			kakaoApplication = applicationRepository.save(
				convertToApplicationEntity(userId, KAKAO_APPLICATION.toDomain()));
		}

		@DisplayName("여러 유저의 공고가 존재하여도")
		@Nested
		class Context_with_many_user_applications {

			private Long otherUserId = -1L;
			private ApplicationJpaEntity otherUserApplication;

			@BeforeEach
			void setUp() {
				otherUserApplication = applicationRepository.save(
					convertToApplicationEntity(otherUserId, NAVER_APPLICATION.toDomain()));

				applicationProcessRepository.save(convertToApplicationProcessEntity(naverApplication,
					TO_APPLY.toDomainWithSchedule(LocalDate.of(year, month, 11)), 1));

				applicationProcessRepository.save(convertToApplicationProcessEntity(kakaoApplication,
					TO_APPLY.toDomainWithSchedule(LocalDate.of(year, month, 20)), 1));

				applicationProcessRepository.save(convertToApplicationProcessEntity(otherUserApplication,
					TO_APPLY.toDomainWithSchedule(LocalDate.of(year, month, 11)), 1));
			}

			@DisplayName("특정 유저의 데이터만 반환한다.")
			@Test
			void it_returns_only_data_for_a_specific_user() {
				List<ApplicationJpaEntity> actual = applicationRepository.findByUserIdAndDate(userId, year, month);

				assertThat(actual).containsOnly(naverApplication, kakaoApplication);
			}
		}

		@DisplayName("여러 달에 일정이 존재할 경우")
		@Nested
		class Context_with_many_schedules_for_several_months {

			private final int otherMonth = 10;
			private List<ApplicationProcessJpaEntity> expectedProcesses = new ArrayList<>();

			@BeforeEach
			void setUp() {
				ApplicationProcessJpaEntity expectedProcess1 = applicationProcessRepository.save(
					convertToApplicationProcessEntity(naverApplication,
						TO_APPLY.toDomainWithSchedule(LocalDate.of(year, month, 11)), 1));

				ApplicationProcessJpaEntity expectedProcess2 = applicationProcessRepository.save(
					convertToApplicationProcessEntity(kakaoApplication,
						TO_APPLY.toDomainWithSchedule(LocalDate.of(year, month, 20)), 1));

				applicationProcessRepository.save(convertToApplicationProcessEntity(kakaoApplication,
					TO_APPLY.toDomainWithSchedule(LocalDate.of(year, otherMonth, 20)), 1));

				expectedProcesses.addAll(List.of(expectedProcess1, expectedProcess2));

				entityManager.clear();
			}

			@DisplayName("조회하는 달의 일정 데이터만 반환한다.")
			@Test
			void it_returns_schedules_for_month() {
				List<ApplicationJpaEntity> response = applicationRepository.findByUserIdAndDate(userId, year, month);

				List<ApplicationProcessJpaEntity> actualProcesses = new ArrayList<>();
				for (ApplicationJpaEntity application : response) {
					actualProcesses.addAll(application.getProcesses());
				}

				assertThat(actualProcesses).hasSize(expectedProcesses.size());
			}
		}

		@Nested
		@DisplayName("한 달에 동일한 공고의 일정이 2개가 있으면")
		class Context_with_application_that_has_two_processes {

			private List<ApplicationProcessJpaEntity> expectedProcesses = new ArrayList<>();

			@BeforeEach
			void setUp() {
				ApplicationProcessJpaEntity expectedProcess1 = applicationProcessRepository.save(
					convertToApplicationProcessEntity(naverApplication,
						TO_APPLY.toDomainWithSchedule(LocalDate.of(year, month, 11)), 1));

				ApplicationProcessJpaEntity expectedProcess2 = applicationProcessRepository.save(
					convertToApplicationProcessEntity(naverApplication,
						DOCUMENT.toDomainWithSchedule(LocalDate.of(year, month, 20)), 1));

				expectedProcesses.addAll(List.of(expectedProcess1, expectedProcess2));

				entityManager.clear();
			}

			@Test
			@DisplayName("2개의 과정을 모두 담은 하나의 지원 공고를 반환한다.")
			void it_return_application_with_two_processes() {
				List<ApplicationJpaEntity> response = applicationRepository.findByUserIdAndDate(userId, year, month);
				List<ApplicationProcessJpaEntity> actual = response.get(0).getProcesses();

				assertThat(actual).hasSize(expectedProcesses.size());
			}
		}
	}
}
