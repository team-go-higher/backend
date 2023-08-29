package gohigher.oauth2.domain;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class OAuth2UserInfo {

	protected final Map<String, Object> attributes;

	public abstract String getOAuth2Id();
	public abstract String getName();
	public abstract String getEmail();
	public abstract Map<String, Object> getAttributes();
}
