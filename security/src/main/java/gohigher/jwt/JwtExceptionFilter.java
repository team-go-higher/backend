package gohigher.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import gohigher.controller.response.GohigherResponse;
import gohigher.global.exception.GoHigherException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;
	private final String allowedOrigin;

	public JwtExceptionFilter(@Value("${cors-config.allowed-origin}") String allowedOrigin) {
		this.objectMapper = new ObjectMapper();
		this.allowedOrigin = allowedOrigin;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (GoHigherException e) {
			response.setStatus(e.getStatusCode());
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.addHeader("Access-Control-Allow-Origin", allowedOrigin);
			response.addHeader("Access-Control-Allow-Credentials", "true");

			GohigherResponse<Object> errorResponse = GohigherResponse.fail(e.getErrorCode(), e.getMessage());

			response.getWriter()
				.write(objectMapper.writeValueAsString(errorResponse));
		}
	}
}
