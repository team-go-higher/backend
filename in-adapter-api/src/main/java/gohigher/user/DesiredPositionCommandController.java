package gohigher.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gohigher.auth.support.Login;
import gohigher.user.port.in.DesiredPositionCommandPort;
import gohigher.user.port.in.DesiredPositionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DesiredPositionCommandController implements DesiredPositionCommandControllerDocs {

	private final DesiredPositionCommandPort desiredPositionCommandPort;

	@PostMapping("/v1/desired-positions")
	public ResponseEntity<Void> saveDesiredPositions(@Login Long userId,
		@RequestBody @Valid DesiredPositionRequest request) {
		desiredPositionCommandPort.saveDesiredPositions(userId, request.getPositionIds());
		return ResponseEntity.noContent().build();
	}
}
