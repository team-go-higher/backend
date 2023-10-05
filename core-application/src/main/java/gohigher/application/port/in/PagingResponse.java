package gohigher.application.port.in;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagingResponse<E> {

	private final boolean lastPage;
	private final List<E> content;
}
