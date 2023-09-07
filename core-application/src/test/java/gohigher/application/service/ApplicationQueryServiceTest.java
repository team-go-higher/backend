package gohigher.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gohigher.application.Application;
import gohigher.application.port.in.ApplicationMonthQueryResponse;
import gohigher.application.port.out.persistence.ApplicationPersistenceQueryPort;
import gohigher.common.EmploymentType;
import gohigher.common.Process;
import gohigher.common.ProcessType;

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

	@DisplayName("findByIdAndMonth 메서드는")
	@Nested
	class Describe_findByIdAndMonth {

		private final long userId = 1L;
		private final int year = 2023;
		private final int month = 9;

		@DisplayName("해당 연/월에 유저가 등록한 지원서 일정이 정보가 있을 때")
		@Nested
		class Context_with_schedules {

			private final LocalDate searchDate = LocalDate.of(year, month, 20);
			private final Process toApply = new Process(ProcessType.TO_APPLY, null,
				LocalDateTime.of(searchDate, LocalTime.now()));
			private final Process document = new Process(ProcessType.DOCUMENT, null,
				LocalDateTime.of(searchDate, LocalTime.now()));
			private final Process interview = new Process(ProcessType.INTERVIEW, null,
				LocalDateTime.of(searchDate, LocalTime.now()));

			@BeforeEach
			void setUp() {
				List<Process> naverProcesses = List.of(toApply, document, interview);
				Application naverApplication = new Application("Naver", "Bakcend", "", "", "", "",
					EmploymentType.PERMANENT, "", "", "", LocalDateTime.now().plusDays(7), naverProcesses, "", toApply);

				List<Process> kakaoProcesses = List.of(toApply, document);
				Application kakaoApplication = new Application("Kakao", "Bakcend", "", "", "", "",
					EmploymentType.PERMANENT, "", "", "", LocalDateTime.now().plusDays(7), kakaoProcesses, "", toApply);

				List<Application> applicationJpaEntities = List.of(naverApplication, kakaoApplication);

				given(applicationPersistenceQueryPort.findByIdAndMonth(userId, year, month))
					.willReturn(applicationJpaEntities);
			}

			@DisplayName("일정 정보를 반환한다.")
			@Test
			void it_return_application_processes() {
				ApplicationMonthQueryResponse actual = applicationQueryService.findByMonth(userId, year, month);

				assertThat(actual.getApplications()).hasSize(5);
			}
		}
	}
}
