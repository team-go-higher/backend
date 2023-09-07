package gohigher.application.entity;

import static gohigher.fixture.ApplicationFixture.*;
import static gohigher.fixture.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

@DisplayName("ApplicationRepository 클래스의")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ApplicationRepositoryTest {

	@Autowired
	ApplicationRepository applicationRepository;

	@Autowired
	ApplicationProcessRepository applicationProcessRepository;

	@Autowired
	EntityManager entityManager;

	@DisplayName("findByUserIdAndMonth 메소드는")
	@Nested
	class Describe_findByUserIdAndMonth {

		private final Long userId = 1L;
		private final Long otherUserId = 2L;
		private final int year = 2023;
		private final int month = 9;
		private final int otherMonth = 10;

		private ApplicationJpaEntity naverApplication;
		private ApplicationJpaEntity kakaoApplication;
		private ApplicationJpaEntity lineApplication;
		private ApplicationJpaEntity otherUserApplication;

		@BeforeEach
		void setUp() {
			naverApplication = applicationRepository.save(NAVER_APPLICATION.toEntity(userId));
			kakaoApplication = applicationRepository.save(KAKAO_APPLICATION.toEntity(userId));
			lineApplication = applicationRepository.save(LINE_APPLICATION.toEntity(userId));
			otherUserApplication = applicationRepository.save(NAVER_APPLICATION.toEntity(otherUserId));
		}

		@DisplayName("여러 유저의 공고가 존재하여도")
		@Nested
		class Context_with_many_user_applications {

			@BeforeEach
			void setUp() {
				applicationProcessRepository.save(
					TO_APPLY.toApplicationProcessEntity(naverApplication, LocalDate.of(year, month, 11)));

				applicationProcessRepository.save(
					TO_APPLY.toApplicationProcessEntity(kakaoApplication, LocalDate.of(year, month, 20)));

				applicationProcessRepository.save(
					TO_APPLY.toApplicationProcessEntity(otherUserApplication, LocalDate.of(year, month, 20)));
			}

			@DisplayName("특정 유저의 데이터만 반환한다.")
			@Test
			void it_returns_only_data_for_a_specific_user() {
				List<ApplicationJpaEntity> actual = applicationRepository.findByUserIdAndMonth(userId, year, month);

				assertThat(actual).hasSize(2);
				assertThat(actual).contains(naverApplication, kakaoApplication);
			}
		}

		@DisplayName("여러 달에 일정이 존재할 경우")
		@Nested
		class Context_with_many_schedules_for_several_months {

			@BeforeEach
			void setUp() {
				applicationProcessRepository.save(
					TO_APPLY.toApplicationProcessEntity(naverApplication, LocalDate.of(year, month, 11)));

				applicationProcessRepository.save(
					TO_APPLY.toApplicationProcessEntity(kakaoApplication, LocalDate.of(year, month, 20)));

				applicationProcessRepository.save(
					TO_APPLY.toApplicationProcessEntity(lineApplication, LocalDate.of(year, otherMonth, 20)));
			}

			@DisplayName("조회하는 달의 일정 데이터만 반환한다.")
			@Test
			void it_returns_schedules_for_month() {
				List<ApplicationJpaEntity> actual = applicationRepository.findByUserIdAndMonth(userId, year, month);

				assertThat(actual).hasSize(2);
				assertThat(actual).contains(naverApplication, kakaoApplication);
			}
		}

		@Nested
		@DisplayName("한 달에 동일한 공고의 일정이 2개가 있으면")
		class Context_with_application_that_has_two_processes {

			@BeforeEach
			void setUp() {
				applicationProcessRepository.save(
					TO_APPLY.toApplicationProcessEntity(naverApplication, LocalDate.of(year, month, 11)));

				applicationProcessRepository.save(
					DOCUMENT.toApplicationProcessEntity(naverApplication, LocalDate.of(year, month, 20)));

				applicationProcessRepository.save(
					INTERVIEW.toApplicationProcessEntity(naverApplication, LocalDate.of(year, otherMonth, 20)));

				entityManager.clear();
			}

			@Test
			@DisplayName("2개성의 과정을 모두 담은 하나의 지원 공고를 반환한다.")
			void it_return_application_with_two_processes() {
				List<ApplicationJpaEntity> actual = applicationRepository.findByUserIdAndMonth(userId, year, month);

				assertThat(actual).hasSize(1);
				assertThat(actual.get(0).getProcesses()).hasSize(2);
			}
		}
	}
}
