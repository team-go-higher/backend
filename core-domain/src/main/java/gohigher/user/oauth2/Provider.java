package gohigher.user.oauth2;

import java.util.Locale;

public enum Provider {
	GOOGLE,
	KAKAO,
	;

	public static Provider from(final String name) {
		return Provider.valueOf(name.toUpperCase(Locale.ROOT));
	}
}
