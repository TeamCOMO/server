package project.como.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.como.domain.post.dto.PostCreateRequestDto;
import project.como.domain.post.dto.PostModifyRequestDto;
import project.como.domain.post.service.PostService;
import project.como.global.auth.model.CurrentUser;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

	private final PostService postService;

	@PostMapping("/create")
	public ResponseEntity<?> createPost(@CurrentUser String username, @RequestBody PostCreateRequestDto dto) {
		return postService.createPost(username, dto);
	}

	@PutMapping("/modify")
	public ResponseEntity<?> modifyPost(@CurrentUser String username, @RequestBody PostModifyRequestDto dto) {
		return postService.modifyPost(username, dto);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deletePost(@CurrentUser String username, @RequestParam Long postId) {
		return postService.deletePost(username, postId);
	}
}
