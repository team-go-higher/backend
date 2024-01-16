package gohigher.application;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gohigher.application.port.in.ApplicationCommandPort;
import gohigher.application.port.in.CurrentProcessUpdateRequest;
import gohigher.application.port.in.SimpleApplicationRegisterResponse;
import gohigher.application.port.in.SimpleApplicationRequest;
import gohigher.application.port.in.SimpleApplicationUpdateRequest;
import gohigher.application.port.in.SpecificApplicationRequest;
import gohigher.controller.response.GohigherResponse;
import gohigher.support.auth.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/applications")
@RequiredArgsConstructor
public class ApplicationCommandController implements ApplicationCommandControllerDocs {

	private final ApplicationCommandPort applicationCommandPort;

	@PostMapping("/simple")
	public ResponseEntity<GohigherResponse<SimpleApplicationRegisterResponse>> registerApplicationSimply(
		@Login Long userId, @RequestBody @Valid SimpleApplicationRequest request) {
		SimpleApplicationRegisterResponse response = applicationCommandPort.applySimply(userId, request);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}

	@PostMapping("/specific")
	public ResponseEntity<GohigherResponse<Void>> registerApplicationSpecifically(@Login Long userId,
		@RequestBody @Valid SpecificApplicationRequest request) {
		long applicationId = applicationCommandPort.applySpecifically(userId, request);
		return ResponseEntity.created(URI.create("/v1/applications/" + applicationId))
			.body(GohigherResponse.success(null));
	}

	@PatchMapping("/current-process")
	public ResponseEntity<GohigherResponse<Void>> updateApplicationCurrentProcess(@Login Long userId,
		@RequestBody @Valid CurrentProcessUpdateRequest request) {
		applicationCommandPort.updateCurrentProcess(userId, request);
		return ResponseEntity.ok(GohigherResponse.success(null));
	}

	@PutMapping("/{applicationId}/simple")
	public ResponseEntity<GohigherResponse<Void>> updateSimply(@Login Long userId, @PathVariable Long applicationId,
		@RequestBody @Valid SimpleApplicationUpdateRequest request) {
		applicationCommandPort.updateSimply(userId, applicationId, request);
		return ResponseEntity.ok(GohigherResponse.success(null));
	}

	@DeleteMapping("/{applicationId}")
	public ResponseEntity<GohigherResponse<Void>> deleteApplication(@Login Long userId, @PathVariable Long applicationId) {
		applicationCommandPort.deleteApplication(userId, applicationId);
		return ResponseEntity.noContent().build();
	}
}
