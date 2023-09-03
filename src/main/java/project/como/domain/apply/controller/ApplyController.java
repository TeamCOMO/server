package project.como.domain.apply.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
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

	@Logging(item = "Apply", action = "Post")
	@PostMapping("/{post_id}")
	public ResponseEntity<String> makeApply(@CurrentUser String username, @PathVariable(value = "post_id") Long postId) {
		applyService.makeApply(username, postId);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Apply", action = "Get")
	@GetMapping("/{post_id}")
	public ResponseEntity<Boolean> getApply(@CurrentUser String username, @PathVariable(value = "post_id") Long postId) {
		boolean isApply = applyService.getApply(username, postId);

		return ResponseEntity.ok().body(isApply);
	}
}
