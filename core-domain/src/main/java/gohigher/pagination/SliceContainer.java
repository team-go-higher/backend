package gohigher.pagination;

import org.springframework.data.domain.Slice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SliceContainer<E> {

	private final Slice<E> content;
}
