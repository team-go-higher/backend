package gohigher.acceptance;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import gohigher.auth.support.JwtProvider;
import gohigher.auth.support.TokenType;
import gohigher.support.DatabaseCleanUp;
import gohigher.user.Role;
import gohigher.user.auth.Provider;
import gohigher.user.entity.UserJpaEntity;
import gohigher.user.entity.UserRepository;
import io.restassured.RestAssured;

@SpringBootTest(properties = "spring.session.store-type=none", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

	@LocalServerPort
	int port;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		databaseCleanUp.execute();
	}

	public String signUp(String email, Provider provider) {
		UserJpaEntity savedUser = userRepository.save(new UserJpaEntity(email, Role.GUEST, provider));
		Date today = new Date();
		String accessToken = jwtProvider.createToken(savedUser.getId(), today, TokenType.ACCESS);
		return accessToken;
	}
}
