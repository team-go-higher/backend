package gohigher.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gohigher.application.port.in.ApplicationProcessByProcessTypeResponse;
import gohigher.application.port.in.ApplicationProcessQueryPort;
import gohigher.auth.support.Login;
import gohigher.common.ProcessType;
import gohigher.controller.response.GohigherResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApplicationProcessQueryController implements ApplicationProcessQueryControllerDocs {

	private final ApplicationProcessQueryPort applicationProcessQueryPort;

	@GetMapping("/v1/applications/{applicationId}/processes?&processType={processType}")
	public ResponseEntity<GohigherResponse<List<ApplicationProcessByProcessTypeResponse>>> getApplicationProcessesByApplicationIdAndType(
		@Login Long userId, @PathVariable Long applicationId, @RequestParam ProcessType processType) {
		List<ApplicationProcessByProcessTypeResponse> response =
			applicationProcessQueryPort.findByApplicationIdAndProcessType(userId, applicationId, processType);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}
}
