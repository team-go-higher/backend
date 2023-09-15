package gohigher.application.port.in;

import java.util.List;

public interface ApplicationQueryPort {

	List<CalendarApplicationResponse> findByMonth(CalendarApplicationRequest request);

	List<DateApplicationResponse> findByDate(DateApplicationRequest request);
}
