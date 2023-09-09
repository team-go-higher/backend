package gohigher.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import gohigher.application.port.in.CalenderApplicationMonthResponse;
import gohigher.controller.response.GohigherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지원서")
public interface ApplicationQueryControllerDocs {

	@Operation(summary = "지원서 캘린더 월간 데이터 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "캘린더 월간 데이터 조회 성공"),
		@ApiResponse(responseCode = "400", description = "날짜 정보가 잘못되었음", content = @Content(
			examples = {
				@ExampleObject(name = "잘못된 날짜 정보가 입력되었음", value = """
					{
					"success": false,
					"error": {
						"code": "APPLICATION_011",
						"message": "잘못된 날짜 정보입니다."
					},
					"data": null
					}
					""")
			}))
	})
	ResponseEntity<GohigherResponse<CalenderApplicationMonthResponse>> findByMonth(
		@Parameter(hidden = true) Long userId, @RequestParam int year, @RequestParam int month);
}
