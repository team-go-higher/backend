package gohigher.application.port.in;

public interface ApplicationQueryPort {

	CalenderApplicationMonthResponse findByMonth(CalenderApplicationMonthRequest request);
}
