package project.como.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import project.como.domain.user.dto.MemberLoginRequestDto;
import project.como.domain.user.dto.MemberSignupRequestDto;
import project.como.domain.user.dto.MemberModifyRequestDto;
import project.como.domain.user.model.User;

public interface UserService {

	String signIn(HttpServletRequest request, MemberLoginRequestDto dto);
	void signUp(MemberSignupRequestDto dto) throws Exception;
	boolean checkDuplicate(String username);
	public ResponseEntity<?> reissue(HttpServletRequest request);
	public User getUser(String username);
	public void modify(String username, MemberModifyRequestDto dto);
}
