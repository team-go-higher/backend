package gohigher.application.port.in;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class MyApplicationRequest {

    private final String sort;
    private final List<String> process;
    private final List<Boolean> completed;
    private final String companyName;
}
