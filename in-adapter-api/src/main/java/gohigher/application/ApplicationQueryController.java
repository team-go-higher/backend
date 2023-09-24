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
import gohigher.application.port.in.CalendarApplicationRequest;
import gohigher.application.port.in.CalendarApplicationResponse;
import gohigher.application.port.in.DateApplicationRequest;
import gohigher.application.port.in.DateApplicationResponse;
import gohigher.application.port.in.KanbanApplicationResponse;
import gohigher.auth.support.Login;
import gohigher.controller.response.GohigherResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/applications")
public class ApplicationQueryController implements ApplicationQueryControllerDocs {

	private final ApplicationQueryPort applicationQueryPort;

	@GetMapping("/{applicationId}")
	public ResponseEntity<GohigherResponse<ApplicationResponse>> findById(@Login Long userId,
		@PathVariable Long applicationId) {
		ApplicationResponse response = applicationQueryPort.findById(userId, applicationId);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}

	@GetMapping("/calendar")
	public ResponseEntity<GohigherResponse<List<CalendarApplicationResponse>>> findByMonth(@Login Long userId,
		@RequestParam int year, @RequestParam int month) {
		CalendarApplicationRequest request = new CalendarApplicationRequest(userId, year, month);
		List<CalendarApplicationResponse> response = applicationQueryPort.findByMonth(request);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}

	@GetMapping("/processes")
	public ResponseEntity<GohigherResponse<List<DateApplicationResponse>>> findByDate(@Login Long userId,
		@RequestParam String date) {
		DateApplicationRequest dateApplicationRequest = new DateApplicationRequest(userId, date);
		List<DateApplicationResponse> response = applicationQueryPort.findByDate(dateApplicationRequest);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}

	@GetMapping("/kanban")
	public ResponseEntity<GohigherResponse<List<KanbanApplicationResponse>>> findForKanban(@Login Long userId) {
		List<KanbanApplicationResponse> response = applicationQueryPort.findForKanban(userId);
		return ResponseEntity.ok(GohigherResponse.success(response));
	}
}
