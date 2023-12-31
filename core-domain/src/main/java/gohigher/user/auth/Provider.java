package gohigher.user.auth;

import java.util.Arrays;

import gohigher.global.exception.GoHigherException;

public enum Provider {
	GOOGLE,
	KAKAO,
	;

	public static Provider from(String name) {
		return Arrays.stream(Provider.values())
			.filter(provider -> provider.name().equals(name.toUpperCase()))
			.findFirst()
			.orElseThrow(() -> new GoHigherException(AuthErrorCode.INVALID_PROVIDER));
	}
}
