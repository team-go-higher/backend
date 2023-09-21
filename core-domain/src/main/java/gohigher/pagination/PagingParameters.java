package gohigher.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagingParameters {

	private final int page;
	private final int size;

	public Pageable toPageable() {
		return PageRequest.of(page - 1, size);
	}
}
