package gohigher.position;

import java.util.List;

import org.springframework.http.ResponseEntity;

import gohigher.controller.response.GohigherResponse;
import gohigher.position.port.in.PositionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "직무")
public interface PositionQueryControllerDocs {

	@Operation(summary = "직무 전체 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<GohigherResponse<List<PositionResponse>>> getPositions();
}
