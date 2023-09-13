package gohigher.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gohigher.application.port.in.ApplicationQueryPort;
import gohigher.application.port.in.CalenderApplicationRequest;
import gohigher.application.port.in.CalenderApplicationResponse;
import gohigher.application.port.in.DateApplicationRequest;
import gohigher.application.port.in.DateApplicationResponse;
import gohigher.auth.support.Login;
import gohigher.controller.response.GohigherResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/applications")
public class ApplicationQueryController implements ApplicationQueryControllerDocs {

	private final ApplicationQueryPort applicationQueryPort;

	@GetMapping("/calender")
	public ResponseEntity<GohigherResponse<List<CalenderApplicationResponse>>> findByMonth(@Login Long userId,
		@RequestParam int year, @RequestParam int month) {
		CalenderApplicationRequest request = new CalenderApplicationRequest(userId, year, month);
		List<CalenderApplicationResponse> response = applicationQueryPort.findByMonth(request);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}

	@GetMapping("/calendar")
	public ResponseEntity<GohigherResponse<List<DateApplicationResponse>>> findByDate(@Login Long userId,
		@RequestParam String date) {
		DateApplicationRequest dateApplicationRequest = new DateApplicationRequest(userId, date);
		List<DateApplicationResponse> response = applicationQueryPort.findByDate(dateApplicationRequest);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}
}
