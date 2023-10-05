package gohigher.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import gohigher.application.port.in.ApplicationResponse;
import gohigher.application.port.in.CalendarApplicationResponse;
import gohigher.application.port.in.DateApplicationResponse;
import gohigher.application.port.in.UnscheduledApplicationResponse;
import gohigher.application.port.in.KanbanApplicationResponse;
import gohigher.application.port.in.PagingRequest;
import gohigher.application.port.in.PagingResponse;
import gohigher.auth.support.Login;
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

	@Operation(summary = "지원서 상세 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "캘린더 월간 데이터 조회 성공"),
		@ApiResponse(responseCode = "400", description = "존재하지 않는 지원서임", content = @Content(
			examples = {
				@ExampleObject(name = "존재하지 않는 지원서임", value = """
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
	ResponseEntity<GohigherResponse<ApplicationResponse>> findById(@Login Long userId,
		@PathVariable Long applicationId);

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
	ResponseEntity<GohigherResponse<List<CalendarApplicationResponse>>> findByMonth(
		@Parameter(hidden = true) Long userId, @RequestParam int year, @RequestParam int month);

	@Operation(summary = "지원서 캘린더 일간 데이터 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "캘린더 일간 데이터 조회 성공"),
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
	ResponseEntity<GohigherResponse<List<DateApplicationResponse>>> findByDate(
		@Parameter(hidden = true) Long userId, @RequestParam String date);

	@Operation(summary = "전형일이 작성되어 있지 않은 지원서 목록 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "전형일이 작성되어 있지 않은 지원서 목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "page 또는 size 의 범위가 잘못됨", content = @Content(
			examples = {
				@ExampleObject(name = "잘못된 page 값임", value = """
					{
					"success": false,
					"error": {
						"code": "PAGINATION_001",
						"message": "page 는 1 이상이어야 합니다."
					},
					"data": null
					}
					"""),
				@ExampleObject(name = "잘못된 size 값임", value = """
					{
					"success": false,
					"error": {
						"code": "PAGINATION_002",
						"message": "size 는 1 이상이어야 합니다."
					},
					"data": null
					}
					""")
			}))
	})
	ResponseEntity<GohigherResponse<PagingResponse<UnscheduledApplicationResponse>>> findUnscheduled(
		@Parameter(hidden = true) @Login Long userId, @ModelAttribute PagingRequest request);

	@Operation(summary = "칸반 지원서 목록 조회")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "칸반 지원서 목록 조회 성공")
		}
	)
	ResponseEntity<GohigherResponse<List<KanbanApplicationResponse>>> findForKanban(@Parameter(hidden = true) Long userId);
}
