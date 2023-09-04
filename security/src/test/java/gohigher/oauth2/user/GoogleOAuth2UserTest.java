package gohigher.oauth2.user;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GoogleOAuth2UserTest {

	@DisplayName("google oauth 사용자 객체를 생성한다")
	@Test
	void create() {
		GoogleOAuth2User user = new GoogleOAuth2User(new HashMap<>());

		assertThat(user.oauth2IdAttributeName).isEqualTo("sub");
	}
}
