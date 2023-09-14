package gohigher.application.service;

import static gohigher.application.ApplicationErrorCode.*;
import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
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

import gohigher.application.Application;
import gohigher.application.port.in.CalenderApplicationRequest;
import gohigher.application.port.in.CalenderApplicationResponse;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.common.Process;

@DisplayName("ApplicationQueryService 클래스의")
@ExtendWith(MockitoExtension.class)
class ApplicationQueryServiceTest {

	@Mock
	private ApplicationPersistenceQueryPort applicationPersistenceQueryPort;

	private ApplicationQueryService applicationQueryService;

	@BeforeEach
	void setUp() {
		applicationQueryService = new ApplicationQueryService(applicationPersistenceQueryPort);
	}

	@DisplayName("findById 메서드는")
	@Nested
	class Describe_findById {

		private final long userId = 1L;

		@DisplayName("조회하는 지원서가 존재하지 않을 경우")
		@Nested
		class Context_with_schedules {

			private final long applicationId = 1L;

			@BeforeEach
			void setUp() {
				Application naverApplication = NAVER_APPLICATION.toDomain();
				given(applicationPersistenceQueryPort.findByIdAndUserId(applicationId, userId))
					.willReturn(Optional.of(naverApplication));
			}

			@DisplayName("예외가 발생한다.")
			@Test
			void it_with_exist_application() {
				assertDoesNotThrow(() -> applicationQueryService.findById(userId, applicationId));
			}
		}

		@DisplayName("조회하는 지원서가 존재하지 않을 경우")
		@Nested
		class Context_with_not_exist_application {

			private final long notExistApplicationId = 1L;

			@BeforeEach
			void setUp() {
				given(applicationPersistenceQueryPort.findByIdAndUserId(notExistApplicationId, userId))
					.willReturn(Optional.empty());
			}

			@DisplayName("예외가 발생한다.")
			@Test
			void it_throws_exception() {
				assertThatThrownBy(() -> applicationQueryService.findById(userId, notExistApplicationId))
					.hasMessage(APPLICATION_NOT_FOUND.getMessage());
			}
		}
	}

	@DisplayName("findByIdAndMonth 메서드는")
	@Nested
	class Describe_findByIdAndMonth {

		private final long userId = 1L;
		private final int year = 2023;
		private final int month = 9;

		@DisplayName("해당 연/월에 유저가 등록한 지원서 일정이 정보가 있을 때")
		@Nested
		class Context_with_schedules {

			private CalenderApplicationRequest request = new CalenderApplicationRequest(userId, year, month);
			private List<Process> naverProcesses;
			private List<Process> kakaoProcesses;

			@BeforeEach
			void setUp() {
				LocalDate searchDate = LocalDate.of(year, month, 20);
				Process toApply = TO_APPLY.toDomainWithSchedule(searchDate);
				Process document = DOCUMENT.toDomainWithSchedule(searchDate);
				Process interview = INTERVIEW.toDomainWithSchedule(searchDate);

				naverProcesses = List.of(toApply, document, interview);
				Application naverApplication = NAVER_APPLICATION.toDomain(naverProcesses, toApply);

				kakaoProcesses = List.of(toApply, document);
				Application kakaoApplication = KAKAO_APPLICATION.toDomain(kakaoProcesses, toApply);

				given(applicationPersistenceQueryPort.findByIdAndMonth(userId, year, month))
					.willReturn(List.of(naverApplication, kakaoApplication));
			}

			@DisplayName("일정 정보를 반환한다.")
			@Test
			void it_return_application_processes() {
				List<CalenderApplicationResponse> actual = applicationQueryService.findByMonth(request);

				assertThat(actual).hasSize(naverProcesses.size() + kakaoProcesses.size());
			}
		}
	}
}
