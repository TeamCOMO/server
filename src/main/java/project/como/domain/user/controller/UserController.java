package project.como.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.como.domain.user.dto.MemberLoginRequestDto;
import project.como.domain.user.dto.MemberSignupRequestDto;
import project.como.domain.user.service.UserService;
import project.como.global.auth.TokenInfo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody MemberSignupRequestDto dto) throws Exception {
		userService.signUp(dto);

		return ResponseEntity.ok("success");
	}

	@PostMapping("/login")
	public TokenInfo login(@RequestBody MemberLoginRequestDto dto) {
		String userId = dto.getUserId();
		String password = dto.getPassword();
		TokenInfo tokenInfo = userService.login(userId, password);

		return tokenInfo;
	}

	@PostMapping("/test")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("success");
	}
}
