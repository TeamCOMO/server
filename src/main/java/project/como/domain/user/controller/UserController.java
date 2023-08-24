package project.como.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.como.domain.user.dto.MemberLoginRequestDto;
import project.como.domain.user.dto.MemberSignupRequestDto;
import project.como.domain.user.model.User;
import project.como.domain.user.service.CustomUserDetailsService;
import project.como.domain.user.service.UserServiceImpl;
import project.como.global.auth.model.CurrentUser;
import project.como.global.auth.service.JwtProvider;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserServiceImpl userServiceImpl;
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtProvider jwtProvider;

	private static final String NOT_DUPLICATED = "사용 가능한 ID입니다.";
	private static final String DUPLICATED = "중복된 ID입니다. 다른 ID를 사용해주세요.";

	@PostMapping("/sign-up")
	public ResponseEntity<String> signUp(@Valid @RequestBody MemberSignupRequestDto dto) throws Exception {
		userServiceImpl.signUp(dto);

		return ResponseEntity.ok().body("success");
	}

	@GetMapping("/sign-in")
	public ResponseEntity<?> signIn(HttpServletRequest request, @Valid @RequestBody MemberLoginRequestDto dto) {
		String accessToken = userServiceImpl.signIn(request, dto);

		return ResponseEntity.ok().body(accessToken);
	}

	@GetMapping("/check-duplicate/{username}")
	public ResponseEntity<String> checkDuplicate(@PathVariable String username) {
		boolean check = userServiceImpl.checkDuplicate(username);

		if (check) return ResponseEntity.status(HttpStatus.CONFLICT).body(DUPLICATED);
		return ResponseEntity.ok().body(NOT_DUPLICATED);
	}

	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok().body("pong");
	}
}
