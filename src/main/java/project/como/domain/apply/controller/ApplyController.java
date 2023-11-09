package project.como.domain.apply.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import project.como.domain.apply.dto.ApplyListResponseDto;
import project.como.domain.apply.service.ApplyService;
import project.como.global.auth.model.CurrentUser;
import project.como.global.common.model.Logging;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/apply")
public class ApplyController {
	private final ApplyService applyService;
	private static final String APPLY_CHECK_API_ENDPOINT = "/api/v1/apply/check/";

	@Logging(item = "Apply", action = "Post")
	@PostMapping("/{post_id}")
	public ResponseEntity<String> makeApply(@CurrentUser String username, @PathVariable(value = "post_id") Long postId) {
		URI location = URI.create(APPLY_CHECK_API_ENDPOINT + applyService.create(username, postId));

		return ResponseEntity.created(location).build();
	}

	@Logging(item = "Apply", action = "Get")
	@GetMapping("/check/{post_id}")
	public ResponseEntity<Boolean> getApply(@CurrentUser String username, @PathVariable(value = "post_id") Long postId) {
		return ResponseEntity.ok().body(applyService.check(username, postId));
	}

	@Logging(item = "Apply", action = "Get")
	@GetMapping("/{post_id}")
	public ResponseEntity<ApplyListResponseDto> getAllByWriter(@CurrentUser String username, @PathVariable(value = "post_id") Long postId) {
		return ResponseEntity.ok().body(applyService.getAllByWriter(username, postId));
	}
}
