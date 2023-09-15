package gohigher.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import gohigher.application.port.in.ApplicationProcessByProcessTypeRequest;
import gohigher.application.port.in.ApplicationProcessByProcessTypeResponse;
import gohigher.application.port.in.ApplicationProcessQueryPort;
import gohigher.auth.support.Login;
import gohigher.controller.response.GohigherResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApplicationProcessQueryController implements ApplicationProcessQueryControllerDocs {

	private final ApplicationProcessQueryPort applicationProcessQueryPort;

	@GetMapping("/v1/applications//processes?applicationId={applicationId}&processType={processType}")
	public ResponseEntity<GohigherResponse<List<ApplicationProcessByProcessTypeResponse>>> getApplicationProcessesByApplicationIdAndType(
		@Login Long userId, @ModelAttribute ApplicationProcessByProcessTypeRequest request) {
		GohigherResponse<List<ApplicationProcessByProcessTypeResponse>> body = GohigherResponse.success(
			applicationProcessQueryPort.findByApplicationIdAndProcessType(userId, request));
		return ResponseEntity.ok(body);
	}
}
