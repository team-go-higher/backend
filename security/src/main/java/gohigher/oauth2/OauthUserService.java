package gohigher.oauth2;

import java.util.Collections;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import gohigher.jwt.RoleGrantedAuthority;
import gohigher.oauth2.user.OAuth2UserInfo;
import gohigher.oauth2.user.OAuth2UserInfoFactory;
import gohigher.user.User;
import gohigher.user.auth.Provider;
import gohigher.user.usecase.UserCommandService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthUserService extends DefaultOAuth2UserService {

	private final UserCommandService oAuth2CommandService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Provider provider = extractProvider(userRequest);
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.createFor(provider, oAuth2User.getAttributes());

		User loginUser = oAuth2CommandService.login(oAuth2UserInfo.getEmail(), provider);
		oAuth2UserInfo.setUserId(loginUser.getId());

		return createOAuth2User(oAuth2UserInfo, loginUser);
	}

	private DefaultOAuth2User createOAuth2User(OAuth2UserInfo oAuth2UserInfo, User loginUser) {
		return new DefaultOAuth2User(
			Collections.singleton(
				new RoleGrantedAuthority(loginUser.getRole())
			),
			oAuth2UserInfo.getAttributes(),
			oAuth2UserInfo.getOAuth2IdAttributeName()
		);
	}

	private Provider extractProvider(OAuth2UserRequest userRequest) {
		return Provider.from(
			userRequest
				.getClientRegistration()
				.getRegistrationId()
		);
	}
}
