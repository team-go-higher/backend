package gohigher;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(access = AccessLevel.PRIVATE) // Builder 메서드를 외부에서 사용하지 않으므로, Private 제어자로 지정
@Getter
public class OAuth2Attribute {
	private static final String GOOGLE = "google";

	private final Map<String, Object> attributes; // 사용자 속성 정보를 담는 Map
	private final String attributeKey; // 사용자 속성의 키 값
	private final String email; // 이메일 정보
	private final String name; // 이름 정보
	private final String provider; // 제공자 정보

	// 서비스에 따라 OAuth2Attribute 객체를 생성하는 메서드
	static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
		if (GOOGLE.equals(provider)) {
			return ofGoogle(provider, attributeKey, attributes);
		}
		throw new RuntimeException();
	}

	/*
	 *   Google 로그인일 경우 사용하는 메서드, 사용자 정보가 따로 Wrapping 되지 않고 제공되어,
	 *   바로 get() 메서드로 접근이 가능하다.
	 * */
	private static OAuth2Attribute ofGoogle(String provider, String attributeKey, Map<String, Object> attributes) {
		return OAuth2Attribute.builder()
			.email((String)attributes.get("email"))
			.provider(provider)
			.attributes(attributes)
			.attributeKey(attributeKey)
			.build();
	}

	public Map<String, Object> convertToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", attributeKey);
		map.put("key", attributeKey);
		map.put("email", email);
		map.put("provider", provider);

		return map;
	}
}
