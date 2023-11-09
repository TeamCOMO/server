package project.como.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import project.como.domain.user.dto.MemberLoginRequestDto;
import project.como.domain.user.dto.MemberSignupRequestDto;

public interface UserService {

	String signIn(HttpServletRequest request, MemberLoginRequestDto dto);
	void signUp(MemberSignupRequestDto dto) throws Exception;

	boolean checkDuplicate(String username);
}
