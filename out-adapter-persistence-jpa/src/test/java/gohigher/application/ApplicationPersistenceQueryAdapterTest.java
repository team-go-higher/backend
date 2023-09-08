package gohigher.application;

import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static gohigher.fixtureConverter.ApplicationFixtureConverter.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;

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

	@DisplayName("findByIdAndMonth 메서드는")
	@Nested
	class Describe_findByIdAndMonth {

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

				given(applicationRepository.findByUserIdAndDate(userId, year, month))
					.willReturn(applicationJpaEntities);
			}

			@DisplayName("일정 정보가 담긴 지원서를 반환한다.")
			@Test
			void it_return_application_with_processes() {
				List<Application> applications = applicationPersistenceQueryAdapter.findByIdAndMonth(userId, year,
					month);

				assertAll(
					() -> assertThat(applications).hasSize(2),
					() -> assertThat(applications.get(0).getProcesses()).hasSize(2),
					() -> assertThat(applications.get(1).getProcesses()).hasSize(1)
				);
			}
		}
	}
}
