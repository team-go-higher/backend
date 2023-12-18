package gohigher.acceptance;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import gohigher.user.auth.Provider;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

@DisplayName("User 인수테스트의")
public class UserAcceptanceTest extends AcceptanceTest {

	@DisplayName("로그인 시 회원정보 조회 기능을 테스트한다.")
	@Test
	void find_myInfo() {
		// given
		String email = "azpi@email.com";
		String accessToken = signUp(email, Provider.GOOGLE);
		assignDesiredPositions(accessToken);

		// when
		ValidatableResponse response = RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/v1/users/me")
			.then().log().all();

		// then
		response.statusCode(HttpStatus.OK.value())
			.body("success", equalTo(true))
			.body("data.email", equalTo(email))
			.body("data.desiredPositions.id",
				containsInRelativeOrder(developer.getId().intValue(), designer.getId().intValue()))
			.body("data.desiredPositions.position",
				containsInRelativeOrder(developer.getPosition(), designer.getPosition()));
	}
}
