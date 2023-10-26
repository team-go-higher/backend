package gohigher.application;

import static gohigher.application.ApplicationFixture.*;
import static gohigher.application.ProcessFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import gohigher.common.Process;

@DisplayName("Application 클래스의")
class ApplicationTest {

	@DisplayName("updateSimply 메소드는")
	@Nested
	class Describe_updateSimply {

		@DisplayName("요청받은 정보로")
		@Nested
		class Context_with_target_process {

			private final String companyNameToUpdate = "a";
			private final String positionToUpdate = "positionToUpdate";
			private final String urlToUpdate = "www.update.com";
			private final Long processId = 0L;
			private final LocalDateTime schedule = LocalDateTime.now().plusDays(10);

			private final Process interview = INTERVIEW.toPersistedDomain(processId);
			private final Application naverApplication = NAVER_APPLICATION.toDomain(List.of(interview), interview);

			@DisplayName("필드 정보를 업데이트한다.")
			@Test
			void it_returns_process() {
				naverApplication.updateSimply(companyNameToUpdate, positionToUpdate, urlToUpdate, processId, schedule);

				assertAll(
					() -> assertThat(naverApplication.getCompanyName()).isEqualTo(companyNameToUpdate),
					() -> assertThat(naverApplication.getPosition()).isEqualTo(positionToUpdate),
					() -> assertThat(naverApplication.getUrl()).isEqualTo(urlToUpdate),
					() -> assertThat(naverApplication.getProcessById(processId).getSchedule()).isEqualTo(schedule)
				);
			}
		}
	}
}
