package project.como.domain.user.dto;

import lombok.Data;

@Data
public class MemberLoginRequestDto {
	private String userId;
	private String password;
}
