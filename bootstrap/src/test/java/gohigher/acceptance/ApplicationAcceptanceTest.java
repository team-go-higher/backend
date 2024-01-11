package gohigher.acceptance;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import gohigher.application.port.in.SimpleApplicationProcessRequest;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.common.ProcessType;
import gohigher.user.auth.Provider;
import io.restassured.response.ValidatableResponse;

@DisplayName("Application 인수테스트의")
public class ApplicationAcceptanceTest extends AcceptanceTest {

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

	private int createApplication(String accessToken) {
		SimpleApplicationRequest request = new SimpleApplicationRequest(
			"카카오", "개발자", "url",
			new SimpleApplicationProcessRequest(ProcessType.TO_APPLY.name(), "지원 예정", null));

		ValidatableResponse response = post(accessToken, "/v1/applications/simple", request);

		return response.extract().path("data.id");
	}
}
