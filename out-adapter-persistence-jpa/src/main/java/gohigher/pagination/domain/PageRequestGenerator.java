package gohigher.pagination.domain;

import org.springframework.data.domain.PageRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PageRequestGenerator {

	private static final int DIFFERENCES_PAGES_AND_DB_INDEX = 1;

	public static PageRequest of(int page, int size) {
		return PageRequest.of(page - DIFFERENCES_PAGES_AND_DB_INDEX, size);
	}
}
