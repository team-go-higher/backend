package gohigher.application.port.in;

public interface ApplicationQueryPort {

	CalenderApplicationMonthResponse findByMonth(Long userId, int year, int month);
}
