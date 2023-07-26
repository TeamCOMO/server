package project.como.domain.heart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import project.como.domain.heart.service.HeartService;
import project.como.global.auth.model.CurrentUser;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HeartController {

	private final HeartService heartService;

	@PostMapping("/heart/{post_id}")
	public ResponseEntity<String> makeHeart(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		heartService.makeHeart(username, postId);

		return ResponseEntity.ok().body("success");
	}

	@DeleteMapping("/heart/delete/{post_id}")
	public ResponseEntity<String> deleteHeart(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		heartService.deleteHeart(username, postId);

		return ResponseEntity.ok().body("success");
	}
}
