package gohigher.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import gohigher.application.port.in.CompletedUpdatingRequest;
import gohigher.application.port.in.CompletedUpdatingResponse;
import gohigher.application.port.in.CurrentProcessUpdateRequest;
import gohigher.application.port.in.SimpleApplicationRegisterResponse;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.application.port.in.SimpleApplicationUpdateRequest;
import gohigher.application.port.in.SpecificApplicationRequest;
import gohigher.controller.response.GohigherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
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
			}))})
	ResponseEntity<GohigherResponse<SimpleApplicationRegisterResponse>> registerApplicationSimply(
		@Parameter(hidden = true) Long userId, @RequestBody SimpleApplicationRequest request);

	@Operation(summary = "지원서 상세등록")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "201", description = "상세 지원서 등록 성공",
				headers = @Header(name = "Location", description = "/applications/{applicationId}")),
			@ApiResponse(responseCode = "400", description = "상세 지원서 등록 실패", content = @Content(
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
						"""),
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
						"""),
					@ExampleObject(name = "전형 일정 입력되지 않음", value = """
						{
						"success": false,
						"error": {
							"code": "JOB_INFO_007",
							"message": "전형 일정이 입력되지 않았습니다."
						},
						"data": null
						}
						""")
				}
			))
		}
	)
	ResponseEntity<GohigherResponse<Void>> registerApplicationSpecifically(@Parameter(hidden = true) Long userId,
		SpecificApplicationRequest request);

	@Operation(summary = "현재 진행 전형 변경")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "현재 진행 전형 변경 성공"),
			@ApiResponse(responseCode = "400", description = "현재 진행 전형 변경 실패", content = @Content(
				examples = {
					@ExampleObject(name = "지원서 id 입력되지 않음", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_003",
							"message": "지원서 id가 입력되지 않았습니다."
						},
						"data": null
						}
						"""),
					@ExampleObject(name = "지원서의 전형 id 입력되지 않음", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_004",
							"message": "지원서의 전형 id가 입력되지 않았습니다."
						},
						"data": null
						}
						""")
				})),
			@ApiResponse(responseCode = "404", description = "현재 진행 전형 변경 실패", content = @Content(
				examples = {@ExampleObject(name = "존재하지 않는 지원서", value = """
					{
					"success": false,
					"error": {
						"code": "APPLICATION_001",
						"message": "존재하지 않는 지원서입니다."
					},
					"data": null
					}
					"""),
					@ExampleObject(name = "존재하지 않는 전형 단계", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_002",
							"message": "존재하지 않는 전형입니다."
						},
						"data": null
						}
						""")
				}))})
	ResponseEntity<GohigherResponse<Void>> updateApplicationCurrentProcess(@Parameter(hidden = true) Long userId,
		@RequestBody CurrentProcessUpdateRequest request);

	@Operation(summary = "지원서 간편 수정")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "지원서 간편 수정 성공"),
			@ApiResponse(responseCode = "400", description = "지원서 간편 수정 실패", content = @Content(
				examples = {
					@ExampleObject(name = "지원서 id 입력되지 않음", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_003",
							"message": "지원서 id가 입력되지 않았습니다."
						},
						"data": null
						}
						"""),
					@ExampleObject(name = "지원서의 전형 id 입력되지 않음", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_004",
							"message": "지원서의 전형 id가 입력되지 않았습니다."
						},
						"data": null
						}
						""")
				})),
			@ApiResponse(responseCode = "404", description = "지원서 간편 수정 실패", content = @Content(
				examples = {@ExampleObject(name = "존재하지 않는 지원서", value = """
					{
					"success": false,
					"error": {
						"code": "APPLICATION_001",
						"message": "존재하지 않는 지원서입니다."
					},
					"data": null
					}
					"""),
					@ExampleObject(name = "존재하지 않는 전형 단계", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_002",
							"message": "존재하지 않는 전형입니다."
						},
						"data": null
						}
						""")
				}))})
	ResponseEntity<GohigherResponse<Void>> updateSimply(@Parameter(hidden = true) Long userId, Long applicationId,
		@RequestBody SimpleApplicationUpdateRequest request);

	@Operation(summary = "지원서 상세 수정")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "지원서 상세 수정 성공"),
			@ApiResponse(responseCode = "400", description = "지원서 상세 수정 실패", content = @Content(
				examples = {
					@ExampleObject(name = "지원서 id 입력되지 않음", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_003",
							"message": "지원서 id가 입력되지 않았습니다."
						},
						"data": null
						}
						""")
				})),
			@ApiResponse(responseCode = "404", description = "지원서 상세 수정 실패", content = @Content(
				examples = {@ExampleObject(name = "존재하지 않는 지원서", value = """
					{
					"success": false,
					"error": {
						"code": "APPLICATION_001",
						"message": "존재하지 않는 지원서입니다."
					},
					"data": null
					}
					""")
				}))})
	ResponseEntity<GohigherResponse<Void>> updateSpecifically(@Parameter(hidden = true) Long userId,
		Long applicationId, @RequestBody SpecificApplicationRequest request);

	@Operation(summary = "지원서 완료 여부 변경")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "지원서 완료 여부 변경 성공", content = @Content(
				examples = {
					@ExampleObject(name = "정상 요청의 경우", value = """
						{
						"success": true,
						"error": null,
						"data": {
								"applicationId": 1L,
								"isCompleted": true
							}
						}
						""")
				})),
			@ApiResponse(responseCode = "400", description = "지원서 완료 여부 변경 실패", content = @Content(
				examples = {
					@ExampleObject(name = "기존 상태 그대로 요청한경우", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_020",
							"message": "현재 보여주기/숨기기 상태로의 요청입니다."
						},
						"data": null
						}
						""")
				}))})
	ResponseEntity<GohigherResponse<CompletedUpdatingResponse>> updateCompleted(@Parameter(hidden = true) Long userId,
		Long applicationId, @RequestBody CompletedUpdatingRequest request);

	@Operation(summary = "지원서 삭제")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "지원서 삭제 성공"),
			@ApiResponse(responseCode = "404", description = "지원서 삭제 실패", content = @Content(
				examples = {@ExampleObject(name = "존재하지 않는 지원서", value = """
					{
					"success": false,
					"error": {
						"code": "APPLICATION_001",
						"message": "존재하지 않는 지원서입니다."
					},
					"data": null
					}
					""")
				}))})
	ResponseEntity<GohigherResponse<Void>> deleteApplication(@Parameter(hidden = true) Long userId, Long applicationId);
}
