package gohigher.position;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gohigher.auth.support.Login;
import gohigher.position.port.in.DesiredPositionRequest;
import gohigher.position.port.in.PositionCommandPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PositionCommandController implements PositionCommandControllerDocs {

	private final PositionCommandPort positionCommandPort;

	@PostMapping("/v1/desired-positions")
	public ResponseEntity<Void> saveDesiredPositions(@Login Long userId,
		@RequestBody @Valid DesiredPositionRequest request) {
		positionCommandPort.saveDesiredPositions(userId, request.getPositionIds());
		return ResponseEntity.noContent().build();
	}
}
