package project.como.global.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenInfo {

	private String grantType;
	private String accessToken;
	private String refreshToken;
}
