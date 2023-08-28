package gohigher.oauth2;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {

	private static final String GOOGLE = "google";

	private final Map<String, Object> attributes;
	private final String attributeKey;
	private final String email;
	private final String name;
	private final String provider;

	static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
		if (GOOGLE.equals(provider)) {
			return ofGoogle(provider, attributeKey, attributes);
		}
		throw new RuntimeException();
	}

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
