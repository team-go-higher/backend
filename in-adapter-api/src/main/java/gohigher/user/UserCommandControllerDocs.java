package gohigher.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import gohigher.user.port.in.DesiredPositionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
	ResponseEntity<Void> saveDesiredPositions(@Parameter(hidden = true) Long userId,
		@RequestBody DesiredPositionRequest request);
}
