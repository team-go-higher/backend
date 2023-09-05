package gohigher.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.controller.response.GohigherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지원서")
public interface ApplicationCommandControllerDocs {

	@Operation(summary = "지원서 간편등록")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "간편 지원서 등록 성공"),
			@ApiResponse(responseCode = "400", description = "회사명 혹은 직무가 입력되지 않음")
		}
	)
	ResponseEntity<GohigherResponse<Void>> registerApplication(@Parameter(hidden = true) Long userId,
		@RequestBody SimpleApplicationRequest command);
}
