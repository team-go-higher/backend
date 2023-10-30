package gohigher.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import gohigher.application.port.in.ApplicationProcessByProcessTypeResponse;
import gohigher.application.port.in.SimpleApplicationProcessRequest;
import gohigher.controller.response.GohigherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지원서 전형")
public interface ApplicationProcessCommandControllerDocs {

	@Operation(summary = "전형 이동을 위한 새로운 전형 생성")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "전형 추가 성공"),
		@ApiResponse(responseCode = "400", description = "전형 타입 및 세부 전형명 입력되지 않음", content = @Content(
			examples = {
				@ExampleObject(name = "전형 단계 입력되지 않음", value = """
					{
					"success": false,
					"error": {
						"code": "JOB_INFO_005",
						"message": "전형 단계가 입력되지 않았습니다."
					},
					"data": null
					}
					"""),
				@ExampleObject(name = "세부 전형 입력되지 않음", value = """
					{
					"success": false,
					"error": {
						"code": "JOB_INFO_006",
						"message": "세부 전형이 입력되지 않았습니다."
					},
					"data": null
					}
					""")
			})),
		@ApiResponse(responseCode = "404", description = "지원서가 없는 경우", content = @Content(
			examples = {
				@ExampleObject(name = "지원서가 존재하지 않음", value = """
					{
					"success": false,
					"error": {
						"code": "APPLICATION_001",
						"message": "존재하지 않는 지원서입니다."
					},
					"data": null
					}
					""")
			}))
	})
	ResponseEntity<GohigherResponse<ApplicationProcessByProcessTypeResponse>> registerApplicationProcess(
		@Parameter(hidden = true) Long userId, long applicationId,
		@RequestBody SimpleApplicationProcessRequest request);
}
