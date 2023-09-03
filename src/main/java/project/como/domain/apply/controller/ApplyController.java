package project.como.domain.apply.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
