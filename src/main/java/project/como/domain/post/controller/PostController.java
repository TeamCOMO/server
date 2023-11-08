package project.como.domain.post.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.como.domain.post.dto.PostCreateRequestDto;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.dto.PostModifyRequestDto;
import project.como.domain.post.dto.PostsResponseDto;
import project.como.domain.post.service.PostService;
import project.como.global.auth.model.CurrentUser;
import project.como.global.common.model.Logging;

import java.util.List;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

	private final PostService postService;

	@Logging(item = "Post", action = "post")
	@PostMapping(consumes = {"application/json", "multipart/form-data"})
	public ResponseEntity<String> createPost(@CurrentUser String username,
	                                         @RequestPart @Valid PostCreateRequestDto dto,
	                                         @RequestPart(required = false) @Size(max = 5) @Valid List<MultipartFile> images) {
		postService.createPost(username, dto, images);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Post", action = "get")
	@GetMapping("/{post_id}")
	public ResponseEntity<PostDetailResponseDto> getDetailPost(@PathVariable(value = "post_id", required = true) Long postId) {
		PostDetailResponseDto dto = postService.getDetailPost(postId);

		return ResponseEntity.ok().body(dto);
	}

	@Logging(item = "Post", action = "get")
	@GetMapping
	public ResponseEntity<PostsResponseDto> getPostsByCategory(@RequestParam(required = false) String category,
			@RequestParam(required = false) List<String> stacks,
			@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
		pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
		PostsResponseDto dto = postService.getPostsByCategory(pageNo, category, stacks);

		return ResponseEntity.ok().body(dto);
	}



	@Logging(item = "Post", action = "get")
	@GetMapping("/myself")
	public ResponseEntity<PostsResponseDto> getPostsByMyself(@CurrentUser String username,
	                                                         @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
		pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
		PostsResponseDto dto = postService.getPostsByMyself(username, pageNo);

		return ResponseEntity.ok().body(dto);
	}

	@Logging(item = "Post", action = "patch")
	@PatchMapping(consumes = {"application/json", "multipart/form-data"})
	public ResponseEntity<String> modifyPost(@CurrentUser String username,
	                                         @RequestPart @Valid PostModifyRequestDto dto,
	                                         @RequestPart(required = false) @Size(max = 5) @Valid List<MultipartFile> images) {
		postService.modifyPost(username, dto, images);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Post", action = "delete")
	@DeleteMapping("/{post_id}")
	public ResponseEntity<String> deletePost(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		postService.deletePost(username, postId);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Post", action = "post")
	@PostMapping("/heart/{post_id}")
	public ResponseEntity<String> makeHeart(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		postService.makeHeart(username, postId);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Post", action = "delete")
	@DeleteMapping("/heart/{post_id}")
	public ResponseEntity<String> deleteHeart(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		postService.deleteHeart(username, postId);

		return ResponseEntity.ok().body("success");
	}
}
