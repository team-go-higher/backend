package gohigher.position;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import gohigher.controller.response.GohigherResponse;
import gohigher.position.port.in.PositionQueryPort;
import gohigher.position.port.in.PositionResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PositionQueryController implements PositionQueryControllerDocs {

	private final PositionQueryPort positionQueryPort;

	@GetMapping("/v1/positions")
	public ResponseEntity<GohigherResponse<List<PositionResponse>>> getPositions() {
		List<PositionResponse> response = positionQueryPort.findPositions();
		return ResponseEntity.ok(GohigherResponse.success(response));
	}
}
