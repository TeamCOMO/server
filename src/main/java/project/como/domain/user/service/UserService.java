package project.como.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import project.como.domain.user.dto.MemberLoginRequestDto;
import project.como.domain.user.dto.MemberSignupRequestDto;

public interface UserService {

	ResponseEntity<?> signIn(HttpServletRequest request, MemberLoginRequestDto dto);
	ResponseEntity<?> signUp(MemberSignupRequestDto dto) throws Exception;
}
