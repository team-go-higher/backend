package gohigher.application;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gohigher.application.port.in.ApplicationCommandPort;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.application.port.in.SpecificApplicationRequest;
import gohigher.auth.support.Login;
import gohigher.controller.response.GohigherResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/applications")
@RequiredArgsConstructor
public class ApplicationCommandController implements ApplicationCommandControllerDocs {

	private final ApplicationCommandPort applicationCommandPort;

	@PostMapping("/simple")
	public ResponseEntity<GohigherResponse<Void>> registerApplicationSimply(@Login Long userId,
		@RequestBody @Valid SimpleApplicationRequest request) {
		applicationCommandPort.applySimply(userId, request);
		return ResponseEntity.ok(GohigherResponse.success(null));
	}

	@PostMapping("/specific")
	public ResponseEntity<GohigherResponse<Void>> registerApplicationSpecifically(@Login Long userId,
		@RequestBody @Valid SpecificApplicationRequest request) {
		long applicationId = applicationCommandPort.applySpecifically(userId, request);
		return ResponseEntity.created(URI.create("/applications/" + applicationId))
			.body(GohigherResponse.success(null));
	}
}
