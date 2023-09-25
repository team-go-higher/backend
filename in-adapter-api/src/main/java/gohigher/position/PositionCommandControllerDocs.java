package gohigher.position;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import gohigher.position.port.in.DesiredPositionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "직무")
public interface PositionCommandControllerDocs {

	@Operation(summary = "희망 직무 등록")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "희망 직무 등록 성공"),
		@ApiResponse(responseCode = "400", description = "희망 직무가 이미 게시된 직무 목록에 있음", content = @Content(
			examples = {
				@ExampleObject(name = "직무가 이미 게시되어 있음", value = """
					{
					"success": false,
					"error": {
						"code": "POSITION_011",
						"message": "목록에 존재하는 포지션입니다."
					},
					"data": null
					}
					""")})),
		@ApiResponse(responseCode = "400", description = "Request에 List가 둘 다 비어있을 경우", content = @Content(
			examples = {
				@ExampleObject(name = "직무가 이미 게시되어 있음", value = """
					{
					"success": false,
					"error": {
						"code": "GLOBAL_011",
						"message": "빈 입력값입니다."
					},
					"data": null
					}
					""")}))})
	ResponseEntity<Void> saveDesiredPositions(@Parameter(hidden = true) Long userId,
		@RequestBody DesiredPositionRequest request);
}
