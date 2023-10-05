package gohigher.pagination;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagingContainer<E> {

	private final boolean lastPage;
	private final List<E> content;
}
