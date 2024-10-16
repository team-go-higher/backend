package gohigher.acceptance;

import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import gohigher.application.port.in.CompletedUpdatingRequest;
import gohigher.application.port.in.SimpleApplicationProcessRequest;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.application.port.in.SpecificApplicationProcessRequest;
import gohigher.application.port.in.SpecificApplicationRequest;
import gohigher.common.ProcessType;
import gohigher.user.auth.Provider;
import io.restassured.response.ValidatableResponse;

@DisplayName("Application 인수테스트의")
public class ApplicationAcceptanceTest extends AcceptanceTest {

	@DisplayName("나의 지원현황 모아보기 기능을 테스트한다")
	@Test
	void findAllByUserId_application() {
		// given
		int page = 1;
		int size = 10;
		String sort = "processType";
		String process1 = ProcessType.TO_APPLY.name();
		String process2 = ProcessType.DOCUMENT.name();
		String companyName = "카카오";

		String accessToken = signUp("azpi@email.com", Provider.GOOGLE);
		String uri = String.format(
			"%s?page=%d&size=%d&sort=%s&process=%s&process=%s&companyName=%s",
			"/v1/applications", page, size, sort, process1, process2, companyName);

		// when
		ValidatableResponse response = get(accessToken, uri);

		// then
		response.statusCode(HttpStatus.OK.value())
			.body("success", equalTo(true));
	}

	@DisplayName("지원서 모아보기 기능을 종료 여부 필터링에 해당되는 경우 테스트한다.")
	@Test
	void findAllByUserId_application_filter_completed() {
		// given
		int page = 1;
		int size = 10;
		String sort = "processType";
		String accessToken = signUp("azpi@email.com", Provider.GOOGLE);
		assignDesiredPositions(accessToken);
		SimpleApplicationRequest simpleApplicationRequest = new SimpleApplicationRequest("카카오", "개발자", null,
			new SimpleApplicationProcessRequest("TO_APPLY", "서류전형", null));
		post(accessToken, "/v1/applications/simple", simpleApplicationRequest);

		String uri = String.format(
			"%s?page=%d&size=%d&sort=%s&completed=%s", "/v1/applications", page, size, sort, "false");

		// when
		ValidatableResponse response = get(accessToken, uri);

		// then
		response.statusCode(HttpStatus.OK.value())
			.body("success", equalTo(true))
			.body("data.content.size()", is(1));
	}

	@DisplayName("지원서를 작성하는 기능을 테스트한다")
	@Test
	void create_application() {
		// given
		String accessToken = signUp("azpi@email.com", Provider.GOOGLE);

		SimpleApplicationProcessRequest processRequest = new SimpleApplicationProcessRequest(
			ProcessType.TO_APPLY.name(),
			"지원 예정", null);
		SimpleApplicationRequest simpleApplicationRequest = new SimpleApplicationRequest("카카오", "개발자", "url",
			processRequest);

		// when
		ValidatableResponse response = post(accessToken, "/v1/applications/simple", simpleApplicationRequest);

		// then
		response.statusCode(HttpStatus.OK.value())
			.body("success", equalTo(true))
			.body("data.id", notNullValue())
			.body("data.companyName", equalTo(simpleApplicationRequest.getCompanyName()))
			.body("data.currentProcessSchedule", equalTo(processRequest.getSchedule()))
			.body("data.currentProcessDescription", equalTo(processRequest.getDescription()));
	}

	@DisplayName("지원서를 삭제하는 기능을 테스트한다")
	@Test
	void delete_application() {
		// given
		String accessToken = signUp("azpi@email.com", Provider.GOOGLE);
		int applicationId = createApplication(accessToken);

		// when
		ValidatableResponse response = delete(accessToken, "/v1/applications/" + applicationId);

		// then
		response.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@DisplayName("지원서 상세 수정 기능을 테스트한다.")
	@Test
	void update_specifically() {
		// given
		String accessToken = signUp("azpi@email.com", Provider.GOOGLE);
		int applicationId = createApplication(accessToken);

		SpecificApplicationRequest specificApplicationRequest = new SpecificApplicationRequest("카카오",
			null, null, null, "디자이너", null, null, null, null, null, null, null,
			List.of(new SpecificApplicationProcessRequest("INTERVIEW", "1차 면접", null, true)), null);

		// when
		ValidatableResponse response = put(accessToken, "/v1/applications/" + applicationId + "/specific",
			specificApplicationRequest);

		// then
		response.statusCode(HttpStatus.OK.value());
	}

	@DisplayName("지원서 스위치 요청이 완료되면 상태코드 200과 변경된 상태를 반환한다.")
	@Test
	void update_completed() {
		// given
		String accessToken = signUp("azpi@email.com", Provider.GOOGLE);
		int applicationId = createApplication(accessToken);
		CompletedUpdatingRequest request = new CompletedUpdatingRequest(true);

		// when
		ValidatableResponse response = patch(accessToken, "/v1/applications/" + applicationId + "/finished", request);

		// then
		response.statusCode(HttpStatus.OK.value())
			.body("data.isCompleted", equalTo(request.getIsCompleted()));
	}

	private int createApplication(String accessToken) {
		SimpleApplicationRequest request = new SimpleApplicationRequest(
			"카카오", "개발자", "url",
			new SimpleApplicationProcessRequest(ProcessType.TO_APPLY.name(), "지원 예정", null));

		ValidatableResponse response = post(accessToken, "/v1/applications/simple", request);

		return response.extract().path("data.id");
	}
}
