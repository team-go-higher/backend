package gohigher.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

	private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";
	private static final String SPLIT_REGEX = ",";

	private final List<HandlerMethodArgumentResolver> resolvers;
	private final String allowedOrigin;

	public AuthConfig(List<HandlerMethodArgumentResolver> resolvers,
		@Value("${cors-config.allowed-origin}") String allowedOrigin) {
		this.resolvers = resolvers;
		this.allowedOrigin = allowedOrigin;
	}

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
		resolvers.addAll(this.resolvers);
	}
}
