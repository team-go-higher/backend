package gohigher.oauth2.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import gohigher.controller.response.GohigherResponse;
import gohigher.user.auth.AuthErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		AuthErrorCode errorCode = AuthErrorCode.INVALID_OAUTH_RESPONSE;
		response.setStatus(errorCode.getStatusCode());
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		GohigherResponse<Object> errorResponse = GohigherResponse.fail(errorCode.getErrorCode(),
			errorCode.getMessage());

		response.getWriter()
			.write(objectMapper.writeValueAsString(errorResponse));
	}
}
