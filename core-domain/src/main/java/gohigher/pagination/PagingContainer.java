package gohigher.pagination;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagingContainer<E> {

	private final boolean hasNext;
	private final List<E> content;

	public boolean hasNext() {
		return hasNext;
	}
}
