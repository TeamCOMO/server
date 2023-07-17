package project.como.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import project.como.domain.user.dto.MemberLoginRequestDto;
import project.como.domain.user.dto.MemberSignupRequestDto;
import project.como.global.auth.model.CurrentUser;

public interface UserService {

	ResponseEntity<?> signIn(HttpServletRequest request, MemberLoginRequestDto dto);
	public ResponseEntity<?> signUp(MemberSignupRequestDto dto) throws Exception;
}
