package gohigher.logging;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

	private static final String HTTP_REQUEST_FORMAT = "\n### HTTP REQUEST ###\nMethod: {}\nURI: {}\n";
	private static final String REQUEST_AUTHORIZATION_FORMAT = "Authorization: {}\n";
	private static final String REQUEST_BODY_FORMAT = "Content-Type: {}\nBody: {}\n";
	private static final String HTTP_RESPONSE_FORMAT = "\n### HTTP RESPONSE ###\nStatusCode: {}";
	private static final String HTTP_RESPONSE_WITH_BODY_FORMAT = HTTP_RESPONSE_FORMAT + "\nBody: {}\n";
	private static final String QUERY_COUNTER_FORMAT = "\n### Request Processed ###\n{} {} [Time: {} ms] [Queries: {}]\n";
	private static final String QUERY_STRING_PREFIX = "?";
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String AUTHORIZATION_SPLIT_DELIMITER = " ";
	private static final int AUTHORIZATION_CREDENTIALS_INDEX = 1;
	private static final char MASKED_CHARACTER = '*';

	private final QueryCountInspector queryCountInspector;

	public LoggingFilter(QueryCountInspector queryCountInspector) {
		this.queryCountInspector = queryCountInspector;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
		long startTime = System.currentTimeMillis();
		MDC.put("traceId", UUID.randomUUID().toString());
		queryCountInspector.startCounter();

		filterChain.doFilter(wrappedRequest, wrappedResponse);

		logProcessedRequest(wrappedRequest, wrappedResponse, System.currentTimeMillis() - startTime);
		queryCountInspector.clearCounter();
		MDC.clear();
		wrappedResponse.copyBodyToResponse();
	}

	private void logProcessedRequest(ContentCachingRequestWrapper request,
		ContentCachingResponseWrapper response,
		long duration) {
		logRequest(request);
		logResponse(response);
		logQueryCount(duration, request.getMethod(), request.getRequestURI());
	}

	private void logRequest(ContentCachingRequestWrapper request) {
		String requestBody = new String(request.getContentAsByteArray());

		if (request.getHeader(AUTHORIZATION_HEADER) == null) {
			log.info(HTTP_REQUEST_FORMAT + REQUEST_BODY_FORMAT,
				getRequestUri(request),
				request.getMethod(),
				request.getContentType(),
				requestBody);
			return;
		}

		log.info(HTTP_REQUEST_FORMAT + REQUEST_AUTHORIZATION_FORMAT + REQUEST_BODY_FORMAT,
			getRequestUri(request),
			request.getMethod(),
			mask(request.getHeader(AUTHORIZATION_HEADER)),
			request.getContentType(),
			requestBody);
	}

	private void logResponse(ContentCachingResponseWrapper response) {
		Optional<String> body = getJsonResponseBody(response);

		if (body.isPresent()) {
			log.info(HTTP_RESPONSE_WITH_BODY_FORMAT, response.getStatus(), body.get());
			return;
		}

		log.info(HTTP_RESPONSE_FORMAT, response.getStatus());
	}

	private void logQueryCount(long duration, String method, String uri) {
		Long queryCount = queryCountInspector.getQueryCount();
		Object[] args = new Object[] {method, uri, duration, queryCount};

		if (queryCount >= 10) {
			log.warn(QUERY_COUNTER_FORMAT, args);
			return;
		}

		log.info(QUERY_COUNTER_FORMAT, args);
	}

	private String getRequestUri(ContentCachingRequestWrapper request) {
		String requestURI = request.getRequestURI();
		String queryString = request.getQueryString();

		if (queryString == null) {
			return requestURI;
		}

		return requestURI + QUERY_STRING_PREFIX + queryString;
	}

	private String mask(String authorization) {
		String[] splitValue = authorization.split(AUTHORIZATION_SPLIT_DELIMITER);
		StringBuilder stringBuilder = new StringBuilder(splitValue[AUTHORIZATION_CREDENTIALS_INDEX]);
		IntStream.range(0, stringBuilder.length())
			.forEach(it -> stringBuilder.setCharAt(it, MASKED_CHARACTER));

		return stringBuilder.toString();
	}

	private Optional<String> getJsonResponseBody(ContentCachingResponseWrapper response) {
		if (Objects.equals(response.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
			return Optional.of(new String(response.getContentAsByteArray()));
		}

		return Optional.empty();
	}
}
