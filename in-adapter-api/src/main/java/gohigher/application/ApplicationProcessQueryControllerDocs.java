package gohigher.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

import gohigher.application.port.in.ApplicationProcessByProcessTypeRequest;
import gohigher.application.port.in.ApplicationProcessByProcessTypeResponse;
import gohigher.controller.response.GohigherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지원서 전형")
public interface ApplicationProcessQueryControllerDocs {

	@Operation(summary = "지원서의 전형 타입에 따른 전형들 조회")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 지원서(혹은 다른 사용자의 지원서 조회)", content = @Content(
				examples = {
					@ExampleObject(name = "존재하지 않는 지원서", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_001",
							"message": "존재하지 않는 지원서입니다."
						},
						"data": null
						}
						""")
				})),
			@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(
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
					@ExampleObject(name = "지원서의 전형 type 입력되지 않음", value = """
						{
						"success": false,
						"error": {
							"code": "APPLICATION_005",
							"message": "지원서 전형의 타입이 입력되지 않았습니다."
						},
						"data": null
						}
						""")
				}
			))
		}
	)
	ResponseEntity<GohigherResponse<List<ApplicationProcessByProcessTypeResponse>>> getApplicationProcessesByApplicationIdAndType(
		@Parameter(hidden = true) Long userId, @Parameter(hidden = true) ApplicationProcessByProcessTypeRequest request);
}
