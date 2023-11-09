package project.como.domain.post.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.net.URI;
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
	private static final String POST_API_ENDPOINT = "/api/v1/post/";

	@Logging(item = "Post", action = "post")
	@PostMapping(consumes = {"application/json", "multipart/form-data"})
	public ResponseEntity<String> createPost(@CurrentUser String username,
	                                         @RequestPart @Valid PostCreateRequestDto dto,
	                                         @RequestPart(required = false) @Size(max = 5) @Valid List<MultipartFile> images) {
		URI location = URI.create(POST_API_ENDPOINT + postService.create(username, dto, images));

		return ResponseEntity.created(location).build();
	}

	@Logging(item = "Post", action = "get")
	@GetMapping("/{post_id}")
	public ResponseEntity<PostDetailResponseDto> getDetailPost(@PathVariable(value = "post_id", required = true) Long postId) {
		PostDetailResponseDto dto = postService.getById(postId);

		return ResponseEntity.ok().body(dto);
	}

	@Logging(item = "Post", action = "get")
	@GetMapping
	public ResponseEntity<PostsResponseDto> getPostsByCategory(@RequestParam(required = false) String category,
			@RequestParam(required = false) List<String> stacks,
			@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
		pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
		PostsResponseDto dto = postService.getByCategory(pageNo, category, stacks);

		return ResponseEntity.ok().body(dto);
	}



	@Logging(item = "Post", action = "get")
	@GetMapping("/myself")
	public ResponseEntity<PostsResponseDto> getPostsByMyself(@CurrentUser String username,
	                                                         @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
		pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
		PostsResponseDto dto = postService.getByMyself(username, pageNo);

		return ResponseEntity.ok().body(dto);
	}

	@Logging(item = "Post", action = "patch")
	@PatchMapping(consumes = {"application/json", "multipart/form-data"})
	public ResponseEntity<Void> modifyPost(@CurrentUser String username,
	                                         @RequestPart @Valid PostModifyRequestDto dto,
	                                         @RequestPart(required = false) @Size(max = 5) @Valid List<MultipartFile> images) {
		postService.modify(username, dto, images);

		return ResponseEntity.noContent().build();
	}

	@Logging(item = "Post", action = "delete")
	@DeleteMapping("/{post_id}")
	public ResponseEntity<String> deletePost(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		postService.deleteById(username, postId);

		return ResponseEntity.noContent().build();
	}

	@Logging(item = "Post", action = "post")
	@PostMapping("/{post_id}/heart")
	public ResponseEntity<String> makeHeart(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		postService.makeHeart(username, postId);

		return ResponseEntity.ok().body("success");
	}

	@Logging(item = "Post", action = "delete")
	@DeleteMapping("/{post_id}/heart")
	public ResponseEntity<String> deleteHeart(@CurrentUser String username, @PathVariable(value = "post_id", required = true) Long postId) {
		postService.deleteHeart(username, postId);

		return ResponseEntity.noContent().build();
	}
}
