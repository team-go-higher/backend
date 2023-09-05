package gohigher.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gohigher.application.port.in.ApplicationCommandPort;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.auth.support.AuthenticationPrincipal;
import gohigher.controller.response.GohigherResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApplicationCommandController implements ApplicationCommandControllerDocs {

	private final ApplicationCommandPort applicationCommandPort;

	@PostMapping("/application/simple")
	public ResponseEntity<GohigherResponse<Void>> registerApplication(@AuthenticationPrincipal Long userId,
		@RequestBody @Valid SimpleApplicationRequest command) {
		applicationCommandPort.applySimply(userId, command);
		return ResponseEntity.ok(GohigherResponse.success(null));
	}
}