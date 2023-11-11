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
import project.como.domain.user.dto.MemberModifyRequestDto;
import project.como.domain.user.service.CustomUserDetailsService;
import project.como.domain.user.service.UserService;
import project.como.global.auth.model.CurrentUser;
import project.como.global.auth.service.JwtProvider;
import project.como.global.common.model.Logging;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserService userService;
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtProvider jwtProvider;

	private static final String NOT_DUPLICATED = "사용 가능한 ID입니다.";
	private static final String DUPLICATED = "중복된 ID입니다. 다른 ID를 사용해주세요.";

	@Logging(item = "User", action = "post")
	@PostMapping("/sign-up")
	public ResponseEntity<String> signUp(@Valid @RequestBody MemberSignupRequestDto dto) throws Exception {
		userService.signUp(dto);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "User", action = "get")
	@PostMapping("/sign-in")
	public ResponseEntity<?> signIn(HttpServletRequest request, @Valid @RequestBody MemberLoginRequestDto dto) {
		String accessToken = userService.signIn(request, dto);

		return ResponseEntity.ok().body(accessToken);
	}

	@Logging(item = "User", action = "get")
	@GetMapping("/check-duplicate/{username}")
	public ResponseEntity<String> checkDuplicate(@PathVariable String username) {
		boolean check = userService.checkDuplicate(username);

		if (check) return ResponseEntity.status(HttpStatus.CONFLICT).body(DUPLICATED);
		return ResponseEntity.ok().body(NOT_DUPLICATED);
	}

	@Logging(item = "User", action = "patch")
	@PatchMapping
	public ResponseEntity<Void> modify(@CurrentUser String username,
									   @RequestBody MemberModifyRequestDto dto) {
		userService.modify(username, dto);

		return ResponseEntity.noContent().build();
	}

	@Logging(item = "Ping", action = "get")
	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok().body("pong");
	}
}
