package gohigher.application.port.in;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagingResponse<E> {

	private final int count;
	private final int page;
	private final int size;
	private final List<E> content;
}
