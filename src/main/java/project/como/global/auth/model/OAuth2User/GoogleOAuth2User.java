package project.como.global.auth.model.OAuth2User;

import java.util.Map;

public class GoogleOAuth2User extends OAuth2UserInfo {

	public GoogleOAuth2User(Map<String, Object> atrributes) {
		super(atrributes);
	}

	@Override
	public String getOAuth2Id() {
		return (String) atrributes.get("sub");
	}

	@Override
	public String getEmail() {
		return (String) atrributes.get("email");
	}

	@Override
	public String getName() {
		return (String) atrributes.get("name");
	}
}
