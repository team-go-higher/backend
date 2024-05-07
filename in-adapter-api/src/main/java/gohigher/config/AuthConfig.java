package gohigher.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import gohigher.support.auth.LoginArgumentResolver;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

	private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";
	private static final String SPLIT_REGEX = ",";

	@Autowired
	private LoginArgumentResolver loginArgumentResolver;

	@Value("${cors-config.allowed-origin}")
	private String allowedOrigin;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(allowedOrigin)
			.allowedMethods(ALLOWED_METHOD_NAMES.split(SPLIT_REGEX))
			.allowCredentials(true)
			.exposedHeaders(HttpHeaders.LOCATION);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginArgumentResolver);
	}
}
