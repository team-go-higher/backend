package gohigher.oauth2.user;

import java.util.Map;

import gohigher.global.exception.GoHigherException;
import gohigher.user.auth.AuthErrorCode;

public class KakaoOAuth2User extends OAuth2UserInfo {

	private final Long id;

	public KakaoOAuth2User(Map<String, Object> attributes) {
		super("id", extractValue(attributes));
		id = (Long)attributes.get(oauth2IdAttributeName);
		this.attributes.put(oauth2IdAttributeName, id);
	}

	private static Map<String, Object> extractValue(Map<String, Object> attributes) {
		String keyOfKakaoValue = "kakao_account";
		if (attributes.containsKey(keyOfKakaoValue)) {
			return (Map<String, Object>)attributes.get(keyOfKakaoValue);
		}
		throw new GoHigherException(AuthErrorCode.KAKAO_VALUE_IS_EMPTY);
	}

	@Override
	public String getOAuth2Id() {
		return id.toString();
	}

	@Override
	public String getOAuth2IdAttributeName() {
		return oauth2IdAttributeName;
	}

	@Override
	public String getEmail() {
		return (String)attributes.get("email");
	}

	@Override
	public String getName() {
		return (String)((Map<String, Object>)attributes.get("profile")).get("nickname");
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Long getUserId() {
		return (Long)attributes.get(USER_ID);
	}

	@Override
	public void setUserId(Long userId) {
		attributes.put(USER_ID, userId);
	}
}
