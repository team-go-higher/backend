package gohigher.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import gohigher.controller.response.GohigherResponse;
import gohigher.support.auth.Login;
import gohigher.user.port.in.MyInfoResponse;
import gohigher.user.port.in.UserQueryPort;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserQueryController implements UserQueryControllerDocs {

	private final UserQueryPort userQueryPort;

	@GetMapping("/v1/users/me")
	public ResponseEntity<GohigherResponse<MyInfoResponse>> findMyInfo(@Login Long id) {
		MyInfoResponse myInfoResponse = userQueryPort.findMyInfo(id);
		return ResponseEntity.ok(GohigherResponse.success(myInfoResponse));
	}
}
