package project.como.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import project.como.domain.post.dto.PostCreateRequestDto;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.dto.PostModifyRequestDto;
import project.como.domain.post.dto.PostsResponseDto;
import project.como.domain.post.service.PostService;
import project.como.global.auth.model.CurrentUser;
import project.como.global.common.model.Logging;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {

	private final PostService postService;

	@Logging(item = "Post", action = "post")
	@PostMapping("/post/create")
	public ResponseEntity<String> createPost(@CurrentUser String username, @RequestBody @Valid PostCreateRequestDto dto) {
		postService.createPost(username, dto);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Post", action = "get")
	@GetMapping("/post/{post_id}")
	public ResponseEntity<PostDetailResponseDto> getDetailPost(@PathVariable(value = "post_id", required = true) Long postId) {
		PostDetailResponseDto dto = postService.getDetailPost(postId);

		return ResponseEntity.ok().body(dto);
	}

	@Logging(item = "Post", action = "get")
	@GetMapping("/posts/{category}")
	public ResponseEntity<PostsResponseDto> getPostsByCategory(@PathVariable String category,
			@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            Pageable pageable) {
		pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
		PostsResponseDto dto = postService.getPostsByCategory(pageable, pageNo, category);

		return ResponseEntity.ok().body(dto);
	}

	@Logging(item = "Post", action = "patch")
	@PatchMapping("/post/modify")
	public ResponseEntity<String> modifyPost(@CurrentUser String username, @RequestBody PostModifyRequestDto dto) {
		postService.modifyPost(username, dto);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Post", action = "delete")
	@DeleteMapping("/post/delete/{post_id}")
	public ResponseEntity<String> deletePost(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		postService.deletePost(username, postId);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Post", action = "post")
	@PostMapping("/post/heart/{post_id}")
	public ResponseEntity<String> makeHeart(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		postService.makeHeart(username, postId);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Post", action = "delete")
	@DeleteMapping("/post/heart/delete/{post_id}")
	public ResponseEntity<String> deleteHeart(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		postService.deleteHeart(username, postId);

		return ResponseEntity.ok().body("success");
	}
}