package gohigher.pagination.port.in;

import static gohigher.pagination.PaginationErrorCode.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import gohigher.global.exception.GoHigherException;
import lombok.Getter;

@Getter
public class PagingParameters {

	private final int page;
	private final int size;

	public PagingParameters(int page, int size) {
		validatePageIsOverOne(page);
		this.page = page;
		this.size = size;
	}

	public Pageable toPageable() {
		return PageRequest.of(page - 1, size);
	}

	private void validatePageIsOverOne(int page) {
		if (page < 1) {
			throw new GoHigherException(PAGE_UNDER_ONE);
		}
	}
}
