package gohigher.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gohigher.application.port.in.ApplicationQueryPort;
import gohigher.application.port.in.ApplicationResponse;
import gohigher.application.port.in.CalenderApplicationRequest;
import gohigher.application.port.in.CalenderApplicationResponse;
import gohigher.auth.support.Login;
import gohigher.controller.response.GohigherResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/applications/")
@RequiredArgsConstructor
public class ApplicationQueryController implements ApplicationQueryControllerDocs {

	private final ApplicationQueryPort applicationQueryPort;

	@GetMapping("/{applicationId}")
	public ResponseEntity<GohigherResponse<ApplicationResponse>> findById(@Login Long userId,
		@PathVariable Long applicationId) {
		ApplicationResponse response = applicationQueryPort.findById(userId, applicationId);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}

	@GetMapping("/calendar")
	public ResponseEntity<GohigherResponse<List<CalenderApplicationResponse>>> findByMonth(@Login Long userId,
		@RequestParam int year, @RequestParam int month) {
		CalenderApplicationRequest request = new CalenderApplicationRequest(userId, year, month);
		List<CalenderApplicationResponse> response = applicationQueryPort.findByMonth(request);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}
}
