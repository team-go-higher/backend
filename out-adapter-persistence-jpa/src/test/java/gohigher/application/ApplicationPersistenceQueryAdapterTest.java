package gohigher.application;

import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static gohigher.fixtureConverter.ApplicationFixtureConverter.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.application.entity.ApplicationJpaEntity;
import gohigher.application.entity.ApplicationRepository;

@DisplayName("ApplicationPersistenceQueryAdapter 클래스의")
@ExtendWith(MockitoExtension.class)
class ApplicationPersistenceQueryAdapterTest {

	@Mock
	private ApplicationRepository applicationRepository;

	private ApplicationPersistenceQueryAdapter applicationPersistenceQueryAdapter;

	@BeforeEach
	void setUp() {
		applicationPersistenceQueryAdapter = new ApplicationPersistenceQueryAdapter(applicationRepository);
	}

	@DisplayName("findByIdAndUserId 메서드는")
	@Nested
	class Describe_findByIdAndUserId {

		private final long userId = 1L;

		@DisplayName("존재하는 지원서 id로 조회하면")
		@Nested
		class Context_with_exist_application_id {

			private final long applicationId = 1L;

			@BeforeEach
			void setUp() {
				ApplicationJpaEntity naverApplication = convertToApplicationEntity(userId,
					NAVER_APPLICATION.toDomain());
				given(applicationRepository.findByIdAndUserIdWithProcess(applicationId, userId))
					.willReturn(Optional.of(naverApplication));
			}

			@DisplayName("Optional로 감싸진 Application객체를 반환한다.")
			@Test
			void it_return_application_wrapped_by_optional() {
				Optional<Application> actual = applicationPersistenceQueryAdapter.findByIdAndUserId(applicationId,
					userId);

				assertThat(actual).isNotEmpty();
			}
		}

		@DisplayName("존재하지 않는 지원서 id로 조회하면")
		@Nested
		class Context_with_not_exist_application_id {

			private final long notExistId = 1L;

			@BeforeEach
			void setUp() {
				given(applicationRepository.findByIdAndUserIdWithProcess(notExistId, userId))
					.willReturn(Optional.empty());
			}

			@DisplayName("비어있는 Optional를 반환한다.")
			@Test
			void it_return_empty_optional() {
				Optional<Application> actual = applicationPersistenceQueryAdapter.findByIdAndUserId(notExistId, userId);

				assertThat(actual).isEmpty();
			}
		}
	}

	@DisplayName("findByUserIdAndMonth 메서드는")
	@Nested
	class Describe_findByUserIdAndMonth {

		private final long userId = 1L;
		private final int year = 2023;
		private final int month = 9;

		@DisplayName("해당 연/월에 유저가 등록한 지원서 일정이 정보가 있을 때")
		@Nested
		class Context_with_many_schedules_for_several_months {

			private ApplicationJpaEntity naverApplication;
			private ApplicationJpaEntity kakaoApplication;

			@BeforeEach
			void setUp() {
				naverApplication = convertToApplicationEntity(userId, NAVER_APPLICATION.toDomain());
				naverApplication.addProcess(convertToApplicationProcessEntity(naverApplication,
					TEST.toDomainWithSchedule(LocalDate.of(year, month, 11)), 1));
				naverApplication.addProcess(convertToApplicationProcessEntity(naverApplication,
					TO_APPLY.toDomainWithSchedule(LocalDate.of(year, month, 20)), 1));

				kakaoApplication = convertToApplicationEntity(userId, KAKAO_APPLICATION.toDomain());
				kakaoApplication.addProcess(convertToApplicationProcessEntity(kakaoApplication,
					TO_APPLY.toDomainWithSchedule(LocalDate.of(year, month, 11)), 1));

				List<ApplicationJpaEntity> applicationJpaEntities = List.of(naverApplication, kakaoApplication);

				given(applicationRepository.findByUserIdAndMonth(userId, year, month))
					.willReturn(applicationJpaEntities);
			}

			@DisplayName("일정 정보가 담긴 지원서를 반환한다.")
			@Test
			void it_return_application_with_processes() {
				List<Application> response = applicationPersistenceQueryAdapter.findByUserIdAndMonth(userId, year, month);

				Application actualNaverApplication = findApplication(response, naverApplication);
				Application actualKakaoApplication = findApplication(response, kakaoApplication);

				assertAll(
					() -> assertThat(response).hasSize(2), // naverApplication, kakaoApplication
					() -> assertThat(actualNaverApplication.getProcesses()).hasSize(
						naverApplication.getProcesses().size()),
					() -> assertThat(actualKakaoApplication.getProcesses()).hasSize(
						kakaoApplication.getProcesses().size())
				);
			}

			private Application findApplication(List<Application> response, ApplicationJpaEntity application) {
				return response.stream()
					.filter(it -> it.getCompanyName().equals(application.getCompanyName()))
					.findAny()
					.orElseThrow();
			}
		}
	}

	@DisplayName("findByUserIdAndDate 메서드는")
	@Nested
	class Describe_findByUserIdAndDate {

		private final Long userId = 1L;
		private final LocalDate date = LocalDate.of(2023, 9, 13);

		@DisplayName("해당 날짜에 전형일인 지원이 있을 경우")
		@Nested
		class Context_exist_application_processes_at_date {

			private ApplicationJpaEntity naverApplication;
			private ApplicationJpaEntity kakaoApplication;

			@BeforeEach
			void setUp() {
				naverApplication = convertToApplicationEntity(userId, NAVER_APPLICATION.toDomain());
				naverApplication.addProcess(convertToApplicationProcessEntity(naverApplication,
					TEST.toDomainWithSchedule(date), 1));
				naverApplication.addProcess(convertToApplicationProcessEntity(naverApplication,
					INTERVIEW.toDomainWithSchedule(date), 2));

				kakaoApplication = convertToApplicationEntity(userId, KAKAO_APPLICATION.toDomain());
				kakaoApplication.addProcess(convertToApplicationProcessEntity(kakaoApplication,
					INTERVIEW.toDomainWithSchedule(date), 1));

				List<ApplicationJpaEntity> applicationJpaEntities = List.of(naverApplication, kakaoApplication);

				given(applicationRepository.findByUserIdAndDate(userId, date.atStartOfDay(),
					date.plusDays(1).atStartOfDay())).willReturn(applicationJpaEntities);
			}

			@DisplayName("해당 지원들을 리턴한다")
			@Test
			void it_returns_proper_applications() {
				// given
				List<ApplicationJpaEntity> applicationJpaEntities = List.of(naverApplication, kakaoApplication);

				given(applicationRepository.findByUserIdAndDate(userId, date.atStartOfDay(),
					date.plusDays(1).atStartOfDay())).willReturn(applicationJpaEntities);

				// when
				List<Application> applications = applicationPersistenceQueryAdapter.findByUserIdAndDate(userId,
					date);

				Application actualNaverApplication = applications.stream()
					.filter(it -> it.getCompanyName().equals(naverApplication.getCompanyName()))
					.findAny()
					.orElseThrow();

				Application actualKakaoApplication = applications.stream()
					.filter(it -> it.getCompanyName().equals(kakaoApplication.getCompanyName()))
					.findAny()
					.orElseThrow();

				// then
				assertAll(
					() -> assertThat(applications).hasSize(2),
					() -> assertThat(actualNaverApplication.getProcesses()).hasSize(2),
					() -> assertThat(actualKakaoApplication.getProcesses()).hasSize(1)
				);
			}
		}
	}
}
