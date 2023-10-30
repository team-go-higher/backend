package gohigher.application.entity;

import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static gohigher.fixtureConverter.ApplicationFixtureConverter.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import gohigher.application.Application;
import gohigher.common.Process;
import gohigher.common.ProcessType;

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
		private ApplicationJpaEntity naverApplicationEntity;

		@DisplayName("지원서가 여러 프로세스를 갖고 있을 경우")
		@Nested
		class Context_with_many_processes {

			private List<ApplicationProcessJpaEntity> naverProcessEntities;

			@BeforeEach
			void setUp() {
				Application naverApplication = NAVER_APPLICATION.toDomain(TO_APPLY, DOCUMENT, TEST, INTERVIEW);

				naverApplicationEntity = saveApplicationAndProcesses(userId, naverApplication);
				naverProcessEntities = naverApplicationEntity.getProcesses();

				entityManager.clear();
			}

			@DisplayName("모든 과정 데이터와 함께 지원서 데이터를 가져온다.")
			@Test
			void it_returns_application_with_processes() {
				Optional<ApplicationJpaEntity> response = applicationRepository.findByIdAndUserIdWithProcess(
					naverApplicationEntity.getId(), userId);

				assertAll(
					() -> assertThat(response).isNotEmpty(),
					() -> assertThat(response.get().getProcesses()).hasSize(naverProcessEntities.size())
				);
			}
		}

		@DisplayName("지원서가 삭제된 경우")
		@Nested
		class Context_with_deleted_application {

			@BeforeEach
			void setUp() {
				Application naverApplication = NAVER_APPLICATION.toDomain();
				naverApplicationEntity = saveApplicationAndProcesses(userId, naverApplication);

				naverApplicationEntity.changeToDelete();
			}

			@DisplayName("비어있는 결과를 반환한다.")
			@Test
			void it_returns_optional_empty() {
				Optional<ApplicationJpaEntity> response = applicationRepository.findByIdAndUserIdWithProcess(
					naverApplicationEntity.getId(), userId);

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

		private ApplicationJpaEntity naverApplicationEntity;
		private ApplicationJpaEntity kakaoApplicationEntity;

		@DisplayName("여러 유저의 공고가 존재하여도")
		@Nested
		class Context_with_many_user_applications {

			private final Long otherUserId = -1L;

			@BeforeEach
			void setUp() {
				Application naverApplication = NAVER_APPLICATION.toDomain(
					TEST.toDomainWithSchedule(LocalDate.of(year, month, 11)));
				naverApplicationEntity = saveApplicationAndProcesses(userId, naverApplication);

				Application kakoaApplication = KAKAO_APPLICATION.toDomain(
					TEST.toDomainWithSchedule(LocalDate.of(year, month, 20)));
				kakaoApplicationEntity = saveApplicationAndProcesses(userId, kakoaApplication);

				Application otherUserApplication = COUPANG_APPLICATION.toDomain(
					TEST.toDomainWithSchedule(LocalDate.of(year, month, 20)));
				saveApplicationAndProcesses(otherUserId, otherUserApplication);
			}

			@DisplayName("특정 유저의 데이터만 반환한다.")
			@Test
			void it_returns_only_data_for_a_specific_user() {
				List<ApplicationJpaEntity> actual = applicationRepository.findByUserIdAndMonth(userId, year, month);

				assertThat(actual).containsOnly(naverApplicationEntity, kakaoApplicationEntity);
			}
		}

		@DisplayName("여러 달에 일정이 존재할 경우")
		@Nested
		class Context_with_many_schedules_for_several_months {

			private final int otherMonth = 10;
			private int expectedNumOfProcesses;

			@BeforeEach
			void setUp() {
				Application kakoaApplication = KAKAO_APPLICATION.toDomain(
					TEST.toDomainWithSchedule(LocalDate.of(year, month, 10)),
					FIRST_INTERVIEW.toDomainWithSchedule(LocalDate.of(year, month, 20)),
					SECOND_INTERVIEW.toDomainWithSchedule(LocalDate.of(year, otherMonth, 20)));
				kakaoApplicationEntity = saveApplicationAndProcesses(userId, kakoaApplication);

				// TEST & FIRST_INTERVIEW
				expectedNumOfProcesses = 2;

				entityManager.clear();
			}

			@DisplayName("조회하는 달의 일정 데이터만 반환한다.")
			@Test
			void it_returns_schedules_for_month() {
				List<ApplicationJpaEntity> response = applicationRepository.findByUserIdAndMonth(userId, year, month);

				List<ApplicationProcessJpaEntity> actualProcesses = new ArrayList<>();
				for (ApplicationJpaEntity application : response) {
					actualProcesses.addAll(application.getProcesses());
				}

				assertThat(actualProcesses).hasSize(expectedNumOfProcesses);
			}
		}

		@DisplayName("한 달에 동일한 공고의 일정이 2개가 있으면")
		@Nested
		class Context_with_application_that_has_two_processes {

			private int expectedNumOfProcesses;

			@BeforeEach
			void setUp() {
				Application naverApplication = NAVER_APPLICATION.toDomain(
					TEST.toDomainWithSchedule(LocalDate.of(year, month, 11)),
					INTERVIEW.toDomainWithSchedule(LocalDate.of(year, month, 20)));
				naverApplicationEntity = saveApplicationAndProcesses(userId, naverApplication);

				// TEST & INTERVIEW
				expectedNumOfProcesses = 2;

				entityManager.clear();
			}

			@DisplayName("2개의 과정을 모두 담은 하나의 지원 공고를 반환한다.")
			@Test
			void it_return_application_with_two_processes() {
				List<ApplicationJpaEntity> response = applicationRepository.findByUserIdAndMonth(userId, year, month);
				List<ApplicationProcessJpaEntity> actual = response.get(0).getProcesses();

				assertThat(actual).hasSize(expectedNumOfProcesses);
			}
		}
	}

	@DisplayName("findByUserIdAndDate 메서드는")
	@Nested
	class Describe_findByUserIdAndDate {

		private final Long userId = 1L;
		private final LocalDate date = LocalDate.of(2023, 9, 13);

		private ApplicationJpaEntity naverApplicationEntity;
		private ApplicationJpaEntity kakaoApplicationEntity;

		@DisplayName("해당 일이 전형일인 지원 전형이 있으면")
		@Nested
		class Context_exist_some_process_at_date {

			@BeforeEach
			void setUp() {
				Application naverApplication = NAVER_APPLICATION.toDomain(TEST.toDomainWithSchedule(date));
				naverApplicationEntity = saveApplicationAndProcesses(userId, naverApplication);

				Application kakoaApplication = KAKAO_APPLICATION.toDomain(TEST.toDomainWithSchedule(date));
				kakaoApplicationEntity = saveApplicationAndProcesses(userId, kakoaApplication);

				entityManager.clear();
			}

			@DisplayName("지원들에 해당 전형들을 넣어 리턴한다")
			@ParameterizedTest
			@ValueSource(longs = {0L, 1L})
			void it_returns_applications_with_proper_processes(long day) {
				// given
				applicationProcessRepository.save(convertToApplicationProcessEntity(naverApplicationEntity,
					TEST.toDomainWithSchedule(date.plusDays(day)), true));    // day = 0일 때는 반환할 전형이 추가

				// when
				List<ApplicationJpaEntity> applicationJpaEntities = applicationRepository.findByUserIdAndDate(userId,
					date.atStartOfDay(), date.plusDays(1).atStartOfDay());

				ApplicationJpaEntity actualNaverApplication = applicationJpaEntities.stream()
					.filter(applicationJpaEntity -> applicationJpaEntity.getCompanyName()
						.equals(naverApplicationEntity.getCompanyName()))
					.findAny()
					.orElseThrow();

				ApplicationJpaEntity actualKakaoApplication = applicationJpaEntities.stream()
					.filter(applicationJpaEntity -> applicationJpaEntity.getCompanyName()
						.equals(kakaoApplicationEntity.getCompanyName()))
					.findAny()
					.orElseThrow();

				// then
				int expectedNaverProcessSize = (day == 0) ? 2 : 1;
				assertAll(
					() -> assertThat(applicationJpaEntities).hasSize(2),
					() -> assertThat(actualNaverApplication.getProcesses()).hasSize(expectedNaverProcessSize),
					() -> assertThat(actualKakaoApplication.getProcesses()).hasSize(1)
				);
			}
		}
	}

	@DisplayName("findUnscheduledByUserId 메서드는")
	@Nested
	class Describe_findUnscheduledByUserId {

		@DisplayName("전형일이 작성되어 있지 않은 프로세스들이 있을 떄")
		@Nested
		class Context_exist_processes_without_schedule {

			@DisplayName("전형일이 등록되어있지 않은 현재 전형만을 포함한 어플리케이션을 반환한다")
			@Test
			void it_return_applications_with_process() {
				// given
				Long userId = 1L;
				PageRequest pageRequest = PageRequest.of(0, 10);

				int applicationCount = 1;
				for (int i = 0; i < applicationCount; i++) {
					Application naverApplication = NAVER_APPLICATION.toDomain(
						TEST.toDomainWithSchedule((LocalDateTime)null),
						FIRST_INTERVIEW.toDomainWithSchedule((LocalDateTime)null),
						SECOND_INTERVIEW.toDomainWithSchedule((LocalDateTime)null)
					);
					saveApplicationAndProcesses(userId, naverApplication);
				}
				entityManager.clear();

				// when
				Slice<ApplicationJpaEntity> applications = applicationRepository.findUnscheduledByUserId(userId,
					pageRequest);

				// then
				assertAll(
					() -> assertThat(applications.getNumberOfElements()).isEqualTo(applicationCount),
					() -> assertThat(applications.getContent().get(0).getProcesses()).hasSize(1)
				);
			}
		}
	}

	@DisplayName("findOnlyWithCurrentProcessByUserId 메서드는")
	@Nested
	class Describe_findOnlyWithCurrentProcessByUserId {

		@DisplayName("어플리케이션이 여러 채용 과정을 갖고 있어도")
		@Nested
		class Context_exist_application {

			@DisplayName("현재 프로세스만을 반환한다")
			@Test
			void it_return_application_current_process() {
				// given
				Long userId = 1L;
				Application naverApplication = NAVER_APPLICATION.toDomain(TO_APPLY, DOCUMENT);
				saveApplicationAndProcesses(userId, naverApplication);
				entityManager.clear();

				// when
				List<ApplicationJpaEntity> applications = applicationRepository.findOnlyWithCurrentProcessByUserId(
					userId);

				// then
				assertThat(applications.get(0).getProcesses()).hasSize(1);
			}
		}

		@DisplayName("삭제된 어플리케이션이 있을 경우")
		@Nested
		class Context_contain_deleted_is_true {

			@DisplayName("반환값에 포함하지 않는다")
			@Test
			void it_not_return() {
				// given
				Long userId = 1L;

				int count = 2;
				for (int i = 0; i < count; i++) {
					ApplicationJpaEntity application = createDeletedApplication(userId);
					applicationRepository.save(application);
				}

				// when
				List<ApplicationJpaEntity> applications = applicationRepository.findOnlyWithCurrentProcessByUserId(
					userId);

				// then
				assertThat(applications).isEmpty();
			}
		}

		private ApplicationJpaEntity createDeletedApplication(Long userId) {
			boolean deleted = true;

			Application application = NAVER_APPLICATION.toDomain();
			return new ApplicationJpaEntity(
				application.getId(), userId, application.getCompanyName(), application.getTeam(),
				application.getLocation(), application.getContact(), application.getPosition(),
				application.getSpecificPosition(), application.getJobDescription(), application.getWorkType(),
				application.getEmploymentType(), application.getCareerRequirement(),
				application.getRequiredCapability(), application.getPreferredQualification(), application.getUrl(),
				null, null, deleted
			);
		}
	}

	@DisplayName("findOnlyCurrentProcessByUserIdAndProcessType 메서드는")
	@Nested
	class Describe_findOnlyCurrentProcessByUserIdAndProcessType {

		@DisplayName("현재 진행중인 상태이며 특정 타입의 프로세스를 가지는 어플리케이션이 있을 경우")
		@Nested
		class Context_exist_current_process_and_process_type {

			@DisplayName("해당 어플리케이션을 반환한다")
			@Test
			void it_return() {
				// given
				Long userId = 1L;
				ProcessType processType = ProcessType.TO_APPLY;

				int applicationCount = 1;
				for (int i = 0; i < applicationCount; i++) {
					ApplicationJpaEntity applicationJpaEntity = applicationRepository.save(
						convertToApplicationEntity(userId, NAVER_APPLICATION.toDomain())
					);

					applicationProcessRepository.save(
						convertToApplicationProcessEntity(applicationJpaEntity, TO_APPLY.toDomain(), true));
					applicationProcessRepository.save(
						convertToApplicationProcessEntity(applicationJpaEntity, DOCUMENT.toDomain(), false));
				}
				entityManager.clear();

				// when
				List<ApplicationJpaEntity> applications = applicationRepository.findOnlyCurrentProcessByUserIdAndProcessType(
					userId, processType);

				// then
				assertAll(
					() -> assertThat(applications.size()).isEqualTo(applicationCount),
					() -> assertThat(applications.get(0).getProcesses()).hasSize(1),
					() -> assertThat(applications.get(0).getProcesses().get(0).getType()).isEqualTo(
						processType)
				);
			}
		}

		@DisplayName("어플리케이션이 삭제되었을 경우")
		@Nested
		class Context_contain_deleted_is_true {

			@DisplayName("반환값에 포함하지 않는다")
			@Test
			void it_not_return() {
				// given
				Long userId = 1L;

				int count = 2;
				for (int i = 0; i < count; i++) {
					ApplicationJpaEntity application = createDeletedApplication(userId);
					applicationRepository.save(application);
				}

				// when
				List<ApplicationJpaEntity> applications = applicationRepository.findOnlyWithCurrentProcessByUserId(
					userId);

				// then
				assertThat(applications).isEmpty();
			}

			private ApplicationJpaEntity createDeletedApplication(Long userId) {
				boolean deleted = true;

				Application application = NAVER_APPLICATION.toDomain();
				return new ApplicationJpaEntity(
					application.getId(), userId, application.getCompanyName(), application.getTeam(),
					application.getLocation(), application.getContact(), application.getPosition(),
					application.getSpecificPosition(), application.getJobDescription(), application.getWorkType(),
					application.getEmploymentType(), application.getCareerRequirement(),
					application.getRequiredCapability(), application.getPreferredQualification(), application.getUrl(),
					null, null, deleted
				);
			}
		}
	}

	public ApplicationJpaEntity saveApplicationAndProcesses(Long userId, Application application) {
		ApplicationJpaEntity applicationEntity = applicationRepository.save(
			convertToApplicationEntity(userId, application));

		for (Process process : application.getProcesses()) {
			ApplicationProcessJpaEntity applicationProcessJpaEntity = applicationProcessRepository.save(
				convertToApplicationProcessEntity(applicationEntity, process,
					application.getCurrentProcess() == process));

			applicationEntity.addProcess(applicationProcessJpaEntity);
		}

		return applicationEntity;
	}
}
