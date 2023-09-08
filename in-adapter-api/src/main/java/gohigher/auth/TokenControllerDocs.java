package gohigher.auth;

import org.springframework.http.ResponseEntity;

import gohigher.auth.response.TokenResponse;
import gohigher.controller.response.GohigherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "토큰")
public interface TokenControllerDocs {

	@Operation(summary = "토큰 발급")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "토큰 발급 성공")}
	)
	ResponseEntity<GohigherResponse<TokenResponse>> authorize(HttpServletResponse response,
		@Parameter(hidden = true) Long userId,
		@Parameter String role);
}
