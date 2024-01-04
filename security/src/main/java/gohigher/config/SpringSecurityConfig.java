package gohigher.config;

import static org.springframework.security.config.Customizer.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import gohigher.jwt.JwtAuthFilter;
import gohigher.jwt.JwtExceptionFilter;
import gohigher.oauth2.OauthUserService;
import gohigher.oauth2.handler.AuthenticationFailureHandler;
import gohigher.oauth2.handler.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	private final OauthUserService oauthUserService;
	private final JwtAuthFilter jwtAuthFilter;
	private final JwtExceptionFilter jwtExceptionFilter;
	private final AuthenticationSuccessHandler oAuth2LoginSuccessHandler;
	private final AuthenticationFailureHandler oAuth2LoginFailureHandler;
	private final String tokenRequestUri;

	public SpringSecurityConfig(OauthUserService oauthUserService, JwtAuthFilter jwtAuthFilter,
		JwtExceptionFilter jwtExceptionFilter,
		AuthenticationSuccessHandler oAuth2LoginSuccessHandler,
		AuthenticationFailureHandler oAuth2LoginFailureHandler,
		@Value("${token.request.uri}") String tokenRequestUri) {
		this.oauthUserService = oauthUserService;
		this.jwtAuthFilter = jwtAuthFilter;
		this.jwtExceptionFilter = jwtExceptionFilter;
		this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
		this.oAuth2LoginFailureHandler = oAuth2LoginFailureHandler;
		this.tokenRequestUri = tokenRequestUri;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(CsrfConfigurer::disable)
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth ->
				auth.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
					.requestMatchers("**" + tokenRequestUri + "/**", "/api-docs", "/swagger-ui/**",
						"/v3/api-docs/swagger-config", "/v3/api-docs"
					).permitAll()
					.anyRequest().authenticated()
			)
			.httpBasic(withDefaults());

		http.oauth2Login(oauth2 -> oauth2
			.redirectionEndpoint(redirection -> redirection.baseUri("/oauth2/callback/**"))
			.userInfoEndpoint(userInfo -> userInfo.userService(oauthUserService))
			.successHandler(oAuth2LoginSuccessHandler)
			.failureHandler(oAuth2LoginFailureHandler));

		return http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
			.build();
	}
}
