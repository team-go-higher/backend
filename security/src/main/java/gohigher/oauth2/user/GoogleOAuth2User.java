package gohigher.oauth2.user;

import java.util.Map;

public class GoogleOAuth2User extends OAuth2UserInfo {

	public GoogleOAuth2User(final Map<String, Object> attributes) {
		super("sub", attributes);
	}

	@Override
	public String getOAuth2Id() {
		return (String)attributes.get(oauth2IdAttributeName);
	}

	@Override
	public String getOAuth2IdAttributeName() {
		return oauth2IdAttributeName;
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}

	@Override
	public String getEmail() {
		return (String)attributes.get("email");
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
