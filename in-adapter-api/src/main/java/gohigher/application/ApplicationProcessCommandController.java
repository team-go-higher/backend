package gohigher.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gohigher.application.port.in.ApplicationProcessByProcessTypeResponse;
import gohigher.application.port.in.ApplicationProcessCommandPort;
import gohigher.application.port.in.UnscheduledProcessRequest;
import gohigher.auth.support.Login;
import gohigher.controller.response.GohigherResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApplicationProcessCommandController implements ApplicationProcessCommandControllerDocs {

	private final ApplicationProcessCommandPort applicationProcessCommandPort;

	@PostMapping("/v1/applications/{applicationId}/processes")
	public ResponseEntity<GohigherResponse<ApplicationProcessByProcessTypeResponse>> registerApplicationProcess(
		@Login Long userId, @PathVariable Long applicationId,
		@Valid @RequestBody UnscheduledProcessRequest request) {
		ApplicationProcessByProcessTypeResponse response = applicationProcessCommandPort.register(userId, applicationId,
			request);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}

}
