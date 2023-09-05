package gohigher.auth;

import java.util.Enumeration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationExtractor {

	private static final String AUTHORIZATION = "Authorization";
	private static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";
	private static final String BEARER_TYPE = "Bearer";

	public static String extract(HttpServletRequest request) {
		Enumeration<String> headers = extractHeaders(request);
		while (headers.hasMoreElements()) {
			String value = headers.nextElement();
			if (isBearerToken(value)) {
				String token = value.substring(BEARER_TYPE.length()).trim();
				request.setAttribute(ACCESS_TOKEN_TYPE, value.substring(0, BEARER_TYPE.length()).trim());
				return parseToken(token);
			}
		}
		return null;
	}

	private static Enumeration<String> extractHeaders(HttpServletRequest request) {
		return request.getHeaders(AUTHORIZATION);
	}

	private static boolean isBearerToken(String value) {
		return value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase());
	}

	private static String parseToken(String token) {
		int commaIndex = token.indexOf(',');
		if (commaIndex > 0) {
			return token.substring(0, commaIndex);
		}
		return token;
	}
}
