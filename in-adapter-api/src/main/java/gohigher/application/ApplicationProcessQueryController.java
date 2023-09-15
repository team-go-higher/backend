package gohigher.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gohigher.application.port.in.ApplicationProcessByProcessTypeResponse;
import gohigher.application.port.in.ApplicationProcessQueryPort;
import gohigher.auth.support.Login;
import gohigher.common.ProcessType;
import gohigher.controller.response.GohigherResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApplicationProcessQueryController {

	private final ApplicationProcessQueryPort applicationProcessQueryPort;

	@GetMapping("/v1/applications/{applicationId}/processes?type={processType}")
	public ResponseEntity<GohigherResponse<List<ApplicationProcessByProcessTypeResponse>>> getApplicationProcessesByApplicationIdAndType(
		@Login Long userId, @PathVariable Long applicationId, ProcessType type) {
		GohigherResponse<List<ApplicationProcessByProcessTypeResponse>> body = GohigherResponse.success(
			applicationProcessQueryPort.findByApplicationIdAndProcessType(userId, applicationId, type));
		return ResponseEntity.ok(body);
	}
}
