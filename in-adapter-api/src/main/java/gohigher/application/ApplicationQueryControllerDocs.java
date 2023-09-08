package gohigher.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import gohigher.application.port.in.CalenderApplicationMonthResponse;
import gohigher.controller.response.GohigherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지원서")
public interface ApplicationQueryControllerDocs {

	@Operation(summary = "지원서 캘린더 월간 데이터 조회")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "캘린더 월간 데이터 조회 성공"),
			@ApiResponse(responseCode = "400", description = "날짜 정보가 잘못되었음")
		}
	)
	ResponseEntity<GohigherResponse<CalenderApplicationMonthResponse>> findByMonth(
		@Parameter(hidden = true) Long userId, @RequestParam int year, @RequestParam int month);
}
