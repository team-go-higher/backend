package gohigher.config;

import static org.springframework.security.config.Customizer.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import gohigher.MyAuthenticationSuccessHandler;
import gohigher.OauthUserService;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfig {

	private final OauthUserService oauthUserService;
	private final MyAuthenticationSuccessHandler oAuth2LoginSuccessHandler;
	// private final MyAuthenticationFailureHandler oAuth2LoginFailureHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(CsrfConfigurer::disable)
			.cors(CorsConfigurer::disable)
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth ->
				auth.requestMatchers("/login", "/h2-console").permitAll()
					.anyRequest().authenticated()
			).httpBasic(withDefaults());

		http.oauth2Login(oauth2 -> oauth2
			.redirectionEndpoint(redirection -> redirection.baseUri("/oauth2/callback/**"))
			.userInfoEndpoint(userInfo -> userInfo.userService(oauthUserService))
			.successHandler(oAuth2LoginSuccessHandler)); // OAuth2 로그인 성공시 처리할 핸들러를 지정해준다.
		// .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패시 처리할 핸들러를 지정해준다.

		return http.build();
	}
}
