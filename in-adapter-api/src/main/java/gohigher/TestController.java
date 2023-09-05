package gohigher;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gohigher.auth.Login;

@RestController
@RequestMapping("/user")
public class TestController {

	@GetMapping
	public ResponseEntity<String> ad(@Login Long userId) {
		return ResponseEntity.ok(userId.toString());
	}
}
