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

import gohigher.oauth2.AuthenticationSuccessHandler;
import gohigher.oauth2.OauthUserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

	private final OauthUserService oauthUserService;
	private final AuthenticationSuccessHandler oAuth2LoginSuccessHandler;

	@Bean
	public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http.csrf(CsrfConfigurer::disable)
			.cors(CorsConfigurer::disable)
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth ->
				auth.anyRequest().authenticated()
			)
			.httpBasic(withDefaults());

		http.oauth2Login(oauth2 -> oauth2
			.redirectionEndpoint(redirection -> redirection.baseUri("/oauth2/callback/**"))
			.userInfoEndpoint(userInfo -> userInfo.userService(oauthUserService))
			.successHandler(oAuth2LoginSuccessHandler));

		return http.build();
	}
}
