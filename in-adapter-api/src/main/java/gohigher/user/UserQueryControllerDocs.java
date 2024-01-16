package gohigher.user;

import org.springframework.http.ResponseEntity;

import gohigher.controller.response.GohigherResponse;
import gohigher.support.auth.Login;
import gohigher.user.port.in.MyInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "사용자")
public interface UserQueryControllerDocs {

	@Operation(summary = "내 정보 조회")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	ResponseEntity<GohigherResponse<MyInfoResponse>> findMyInfo(@Login Long id);
}
