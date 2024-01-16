package gohigher.user;

import org.springframework.http.ResponseEntity;

import gohigher.controller.response.GohigherResponse;
import gohigher.user.port.in.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "토큰")
public interface TokenCommandControllerDocs {

	@Operation(summary = "토큰 갱신")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
		@ApiResponse(responseCode = "400", description = "리프레시 토큰 쿠키가 없는 경우", content = @Content(
			examples = {
				@ExampleObject(name = "쿠키가 없음", value = """
					{
					"success": false,
					"error": {
						"code": "AUTH_031",
						"message": "리프레시 쿠키가 없습니다."
					},
					"data": null
					}
					""")}
		)),
		@ApiResponse(responseCode = "404", description = "해당 사용자의 리프레시 토큰이 없는 경우", content = @Content(
			examples = {
				@ExampleObject(name = "사용자의 리프레시 토큰이 존재하지 않는 경우", value = """
					{
					"success": false,
					"error": {
						"code": "AUTH_004",
						"message": "존재하지 않는 토큰입니다."
					},
					"data": null
					}
					""")}
		)),
		@ApiResponse(responseCode = "400", description = "이미 사용된 토큰인 경우", content = @Content(
			examples = {
				@ExampleObject(name = "이미 사용된 토큰인 경우", value = """
					{
					"success": false,
					"error": {
						"code": "AUTH_005",
						"message": "이미 사용된 리프레시 토큰입니다."
					},
					"data": null
					}
					""")}
		)),
		@ApiResponse(responseCode = "400", description = "만료된 리프레시 토큰인 경우", content = @Content(
			examples = {
				@ExampleObject(name = "만료된 리프레시 토큰인 경우", value = """
					{
					"success": false,
					"error": {
						"code": "AUTH_006",
						"message": "만료된 토큰입니다."
					},
					"data": null
					}
					""")}
		)),
	})
	ResponseEntity<GohigherResponse<TokenResponse>> reissueRefreshTokens(HttpServletRequest request,
		HttpServletResponse response, @Parameter(hidden = true) Long userId);
}
