package gohigher.oauth2.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import gohigher.response.GoHigherResponse;
import gohigher.user.AuthErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		AuthErrorCode e = AuthErrorCode.INVALID_OAUTH_RESPONSE;
		GoHigherResponse<Object> errorResponse = GoHigherResponse.fail(e.getErrorCode(), e.getMessage());

		response.getWriter()
			.write(objectMapper.writeValueAsString(errorResponse));
	}
}
