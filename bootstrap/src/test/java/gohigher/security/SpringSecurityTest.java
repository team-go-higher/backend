package gohigher.security;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Spring Security 의")
@SpringBootTest
public class SpringSecurityTest {

	@DisplayName("registration 생성은")
	@Nested
	class registration {

		private static final String REGISTRATION_PREFIX = "spring.security.oauth2.client.registration.";
		private static final String GOOGLE = REGISTRATION_PREFIX + "google.";
		private static final String KAKAO = REGISTRATION_PREFIX + "kakao.";

		@DisplayName("google registration 을 생성하려 할 때")
		@Nested
		class google {

			@Value("${" + GOOGLE + "client-id}")
			private String clientId;

			@Value("${" + GOOGLE + "client-secret}")
			private String clientSecret;

			@Value("${" + GOOGLE + "scope}")
			private String scope;

			@Value("${" + GOOGLE + "redirect_uri}")
			private String redirectUri;

			@DisplayName("필수 설정을 선언해야 한다.")
			@Test
			void success() {
				// then
				assertAll(
					() -> assertThat(clientId).isNotNull(),
					() -> assertThat(clientSecret).isNotNull(),
					() -> assertThat(scope).isNotNull(),
					() -> assertThat(redirectUri).isNotNull()
				);
			}
		}

		@DisplayName("kakao registration 을 생성하려 할 때")
		@Nested
		class kakao {

			@Value("${" + KAKAO + "client-id}")
			private String clientId;

			@Value("${" + KAKAO + "redirect-uri}")
			private String redirectUri;

			@Value("${" + KAKAO + "authorization-grant-type}")
			private String authorizationGrantType;

			@DisplayName("필수 설정을 선언해야 한다.")
			@Test
			void success() {
				// then
				assertAll(
					() -> assertThat(clientId).isNotNull(),
					() -> assertThat(redirectUri).isNotNull(),
					() -> assertThat(authorizationGrantType).isNotNull()
				);
			}
		}

		@DisplayName("kakao oauth 서버에 사용자 정보를 통신하려 할 때")
		@Nested
		class kakaoUser {

			@Value("${" + KAKAO + "scope}")
			private String scope;

			@Value("${" + KAKAO + "client-name}")
			private String clientName;

			@Value("${" + KAKAO + "client-authentication-method}")
			private String clientAuthenticationMethod;

			@DisplayName("추가 설정을 선언해야 한다.")
			@Test
			void success() {
				// then
				assertAll(
					() -> assertThat(scope).isNotNull(),
					() -> assertThat(clientName).isNotNull(),
					() -> assertThat(clientAuthenticationMethod).isNotNull()
				);
			}
		}
	}

	@DisplayName("provider 생성은")
	@Nested
	class provider {

		private static final String PROVIDER_PREFIX = "spring.security.oauth2.client.provider.";
		private static final String KAKAO = PROVIDER_PREFIX + "kakao.";

		@DisplayName("kakao provider 를 생성하려 할 때")
		@Nested
		class kakao {

			@Value("${" + KAKAO + "authorization-uri}")
			private String authorizationUri;

			@Value("${" + KAKAO + "token-uri}")
			private String tokenUri;

			@DisplayName("필수 설정을 선언해야 한다.")
			@Test
			void success() {
				// then
				assertAll(
					() -> assertThat(authorizationUri).isNotNull(),
					() -> assertThat(tokenUri).isNotNull()
				);
			}
		}

		@DisplayName("kakao oauth 서버에 사용자 정보를 통신하려 할 때")
		@Nested
		class kakaoUser {

			@Value("${" + KAKAO + "user-info-uri}")
			private String userInfoUri;

			@Value("${" + KAKAO + "user-name-attribute}")
			private String userNameAttribute;

			@DisplayName("필수 설정을 선언해야 한다.")
			@Test
			void success() {
				// then
				assertAll(
					() -> assertThat(userInfoUri).isNotNull(),
					() -> assertThat(userNameAttribute).isNotNull()
				);
			}
		}
	}
}
