package gohigher.position;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gohigher.auth.support.Login;
import gohigher.position.port.in.DesiredPositionRequest;
import gohigher.position.port.in.PositionCommandPort;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PositionCommandController {

	private final PositionCommandPort positionCommandPort;

	@PostMapping("/v1/positions")
	public ResponseEntity<Void> saveDesiredPositions(@Login Long userId, @RequestBody DesiredPositionRequest request) {
		List<Long> personalPositionIds = positionCommandPort.savePersonalPositions(
			userId, request.getPersonalPositions());
		return ResponseEntity.noContent().build();
	}

}
