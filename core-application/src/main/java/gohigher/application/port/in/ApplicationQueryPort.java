package gohigher.application.port.in;

public interface ApplicationQueryPort {

	ApplicationMonthQueryResponse findByMonth(Long userId, int year, int month);
}
