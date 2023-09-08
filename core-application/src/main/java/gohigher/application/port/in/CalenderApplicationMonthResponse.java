package gohigher.application.port.in;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CalenderApplicationMonthResponse {

	private final List<CalenderApplicationResponse> applications;
}
