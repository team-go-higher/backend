package gohigher.application.port.in;

import java.util.List;

public interface ApplicationQueryPort {

	List<CalenderApplicationResponse> findByMonth(CalenderApplicationRequest request);

	ApplicationResponse findById(Long userId, Long applicationId);
}
