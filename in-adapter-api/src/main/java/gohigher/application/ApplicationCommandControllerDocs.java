package gohigher.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.application.port.in.SpecificApplicationRequest;
import gohigher.controller.response.GohigherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지원서")
public interface ApplicationCommandControllerDocs {

	@Operation(summary = "지원서 간편등록")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "간편 지원서 등록 성공"),
		@ApiResponse(responseCode = "400", description = "회사명 혹은 직무가 입력되지 않음", content = @Content(
			examples = {
				@ExampleObject(name = "회사명 입력되지 않음", value = """
					{
					"success": false,
					"error": {
						"code": "JOB_INFO_002",
						"message": "회사명이 입력되지 않았습니다."
					},
					"data": null
					}
					"""),
				@ExampleObject(name = "직무 입력되지 않음", value = """
					{
					"success": false,
					"error": {
						"code": "JOB_INFO_003",
						"message": "직무가 입력되지 않았습니다."
					},
					"data": null
					}
					""")
			}))})
	ResponseEntity<GohigherResponse<Void>> registerApplicationSimply(@Parameter(hidden = true) Long userId,
		@RequestBody SimpleApplicationRequest request);

	@Operation(summary = "지원서 상세등록")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "상세 지원서 등록 성공"),
			@ApiResponse(responseCode = "400", description = "상세 지원서 등록 실패", content = @Content(
				examples = {
					@ExampleObject(name = "유효하지 않은 전형 단계", value = """
						{
						"success": false,
						"error": {
							"code": "JOB_INFO_001",
							"message": "유효하지 않은 전형 단계입니다."
						},
						"data": null
						}
						"""),
					@ExampleObject(name = "유효하지 않은 고용 형태", value = """
						{
						"success": false,
						"error": {
							"code": "JOB_INFO_004",
							"message": "유효하지 않은 고용 형태입니다."
						},
						"data": null
						}
						"""),
					@ExampleObject(name = "전형 단계 입력되지 않음", value = """
						{
						"success": false,
						"error": {
							"code": "JOB_INFO_005",
							"message": "전형 단계가 입력되지 않았습니다."
						},
						"data": null
						}
						""")
				}))})
	ResponseEntity<GohigherResponse<Void>> registerApplicationSpecifically(@Parameter(hidden = true) Long userId,
		SpecificApplicationRequest request);
}
