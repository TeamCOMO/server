package project.como.domain.post.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import project.como.global.auth.model.CurrentUser;

@Slf4j
@RestController
public class PostController {

	public ResponseEntity<?> createPost(@CurrentUser String userId) {
		log.info("userId : {}", userId);

		return ResponseEntity.ok("success");
	}
}
