package gohigher.acceptance;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import gohigher.application.port.in.SimpleApplicationProcessRequest;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.common.ProcessType;
import gohigher.user.auth.Provider;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

@DisplayName("Application 인수테스트의")
public class ApplicationAcceptanceTest extends AcceptanceTest {

	@DisplayName("지원서를 작성 기능을 테스트한다")
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
		ValidatableResponse response = RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.body(simpleApplicationRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/v1/applications/simple")
			.then().log().all();

		// then
		response.statusCode(HttpStatus.OK.value())
			.body("success", equalTo(true))
			.body("data.id", notNullValue())
			.body("data.companyName", equalTo(simpleApplicationRequest.getCompanyName()))
			.body("data.currentProcessSchedule", equalTo(processRequest.getSchedule()))
			.body("data.currentProcessDescription", equalTo(processRequest.getDescription()));
	}
}
