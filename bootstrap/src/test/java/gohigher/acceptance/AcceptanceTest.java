package gohigher.acceptance;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import gohigher.auth.support.JwtProvider;
import gohigher.auth.support.TokenType;
import gohigher.position.entity.PositionJpaEntity;
import gohigher.position.entity.PositionRepository;
import gohigher.support.DatabaseCleanUp;
import gohigher.user.Role;
import gohigher.user.auth.Provider;
import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;
import gohigher.user.port.in.DesiredPositionRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

@SpringBootTest(properties = "spring.session.store-type=none", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

	@LocalServerPort
	int port;
	PositionJpaEntity developer;
	PositionJpaEntity designer;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PositionRepository positionRepository;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		databaseCleanUp.execute();
		developer = positionRepository.save(new PositionJpaEntity("개발자"));
		designer = positionRepository.save(new PositionJpaEntity("디자이너"));
	}

	String signUp(String email, Provider provider) {
		UserJpaEntity savedUser = userRepository.save(new UserJpaEntity(email, Role.GUEST, provider));
		Date today = new Date();
		return jwtProvider.createToken(savedUser.getId(), today, TokenType.ACCESS);
	}

	void assignDesiredPositions(String accessToken) {
		DesiredPositionRequest desiredPositionRequest = new DesiredPositionRequest(
			List.of(developer.getId(), designer.getId()));
		post(accessToken, "v1/desired-positions", desiredPositionRequest);
	}

	ValidatableResponse post(String accessToken, String uri, Object desiredPositionRequest) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.body(desiredPositionRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().post(uri)
			.then().log().all();
	}

}
