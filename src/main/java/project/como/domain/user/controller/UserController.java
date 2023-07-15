package project.como.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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
//		log.info("id : {}", id);
		userServiceImpl.signUp(dto);

		return ResponseEntity.ok("success");
	}

	@PostMapping("/sign-in")
	public ResponseEntity<?> signIn(HttpServletRequest request, @RequestBody MemberLoginRequestDto dto) {
		return userServiceImpl.signIn(request, dto);
	}

	@PostMapping("/test")
	public ResponseEntity<String> test(@RequestParam String username)
	{
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		log.info("user : {}", userDetails);
		return ResponseEntity.ok("success");
	}
}
