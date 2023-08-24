package project.como.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

	@PostMapping("/sign-up")
	public ResponseEntity<String> signUp(@RequestBody MemberSignupRequestDto dto) throws Exception {
		userServiceImpl.signUp(dto);

		return ResponseEntity.ok().body("success");
	}

	@GetMapping("/sign-in")
	public ResponseEntity<?> signIn(HttpServletRequest request, @RequestBody MemberLoginRequestDto dto) {
		String accessToken = userServiceImpl.signIn(request, dto);

		return ResponseEntity.ok().body(accessToken);
	}

	@GetMapping("/current-test")
	public ResponseEntity<?> getCurrent(@CurrentUser String username) {
		User user = userServiceImpl.getUser(username);

		return ResponseEntity.ok().body(user);
	}

	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok().body("pong");
	}
}
