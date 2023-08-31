package gohigher.oauth2;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import gohigher.usecase.OAuth2CommandService;
import gohigher.user.User;
import gohigher.user.oauth2.OAuth2UserInfo;
import gohigher.user.oauth2.OAuth2UserInfoFactory;
import gohigher.user.oauth2.Provider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthUserService extends DefaultOAuth2UserService {

	private static final String ROLE_PREFIX = "ROLE_";

	private final OAuth2CommandService oAuth2CommandService;

	@Override
	public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Provider provider = extractProvider(userRequest);
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.createFor(provider, oAuth2User.getAttributes());

		User loginUser = oAuth2CommandService.login(oAuth2UserInfo.getEmail(), provider);

		return createOAuth2User(oAuth2UserInfo, loginUser);
	}

	private DefaultOAuth2User createOAuth2User(final OAuth2UserInfo oAuth2UserInfo, final User loginUser) {
		return new DefaultOAuth2User(
			Collections.singleton(
				new SimpleGrantedAuthority(loginUser.getRole().toString())
			),
			oAuth2UserInfo.getAttributes(),
			oAuth2UserInfo.getOAuth2IdAttributeName()
		);
	}

	private Provider extractProvider(final OAuth2UserRequest userRequest) {
		return Provider.from(
			userRequest
				.getClientRegistration()
				.getRegistrationId()
		);
	}
}
