package gohigher.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import gohigher.controller.response.GohigherResponse;
import gohigher.user.port.in.DesiredPositionRequest;
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

@Tag(name = "사용자")
public interface UserCommandControllerDocs {

	@Operation(summary = "희망 직무 등록")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "희망 직무 등록 성공"),
		@ApiResponse(responseCode = "400", description = "Request에 List가 비어있을 경우", content = @Content(
			examples = {
				@ExampleObject(name = "희망 직무가 비었음", value = """
					{
					"success": false,
					"error": {
						"code": "POSITION_011",
						"message": "희망 직무 ID가 비어있습니다."
					},
					"data": null
					}
					""")})),
		@ApiResponse(responseCode = "400", description = "입력받은 positionIds에 중복이 있을 경우", content = @Content(
			examples = {
				@ExampleObject(name = "희망 직무에 중복이 있음", value = """
					{
					"success": false,
					"error": {
						"code": "POSITION_012",
						"message": "중복된 직무 ID를 입력받았습니다."
					},
					"data": null
					}
					""")})),
		@ApiResponse(responseCode = "404", description = "입력받은 positionId가 존재하지 않을 경우", content = @Content(
			examples = {
				@ExampleObject(name = "존재하지 않는 직무를 희망함", value = """
					{
					"success": false,
					"error": {
						"code": "POSITION_001",
						"message": "존재하지 않는 직무입니다."
					},
					"data": null
					}
					""")})),
		@ApiResponse(responseCode = "400", description = "게스트가 아닌 경우", content = @Content(
			examples = {
				@ExampleObject(name = "존재하지 않는 직무를 희망함", value = """
					{
					"success": false,
					"error": {
						"code": "USER_002",
						"message": "게스트가 아닙니다."
					},
					"data": null
					}
					""")}))
	})
	ResponseEntity<Void> saveDesiredPositions(Long userId, @RequestBody DesiredPositionRequest request);

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
	ResponseEntity<GohigherResponse<TokenResponse>> refreshTokens(HttpServletRequest request,
		HttpServletResponse response, @Parameter(hidden = true) Long userId);
}
