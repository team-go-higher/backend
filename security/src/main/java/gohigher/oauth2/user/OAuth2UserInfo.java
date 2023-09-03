package gohigher.oauth2.user;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class OAuth2UserInfo {

	protected static final String USER_ID = "userId";

	protected final String oauth2IdAttributeName;
	protected final Map<String, Object> attributes;

	public abstract String getOAuth2Id();

	public abstract String getOAuth2IdAttributeName();

	public abstract String getName();

	public abstract String getEmail();

	public abstract Map<String, Object> getAttributes();

	public abstract Long getUserId();

	public abstract void setUserId(Long userId);
}
