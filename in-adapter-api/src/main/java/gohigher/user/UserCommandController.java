package gohigher.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gohigher.auth.support.Login;
import gohigher.user.port.in.DesiredPositionRequest;
import gohigher.user.port.in.UserCommandPort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserCommandController implements UserCommandControllerDocs {

	private final UserCommandPort userCommandPort;

	@PostMapping("/v1/desired-positions")
	public ResponseEntity<Void> saveDesiredPositions(@Login Long userId,
		@RequestBody @Valid DesiredPositionRequest request) {
		userCommandPort.updateGuestToUser(userId, request);
		return ResponseEntity.noContent().build();
	}
}
