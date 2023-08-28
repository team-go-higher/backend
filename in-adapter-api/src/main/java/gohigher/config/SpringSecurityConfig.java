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
			.successHandler(oAuth2LoginSuccessHandler));

		return http.build();
	}
}
