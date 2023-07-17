package project.como.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.post.dto.PostCreateRequestDto;
import project.como.domain.post.dto.PostModifyRequestDto;
import project.como.domain.post.model.Post;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;

	public ResponseEntity<?> createPost(String username, PostCreateRequestDto dto) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		Post newPost = Post.builder()
				.title(dto.getTitle())
				.body(dto.getBody())
				.category(dto.getCategory())
				.state(dto.getState())
				.techs(dto.getTechs())
				.user(user)
				.build();

		postRepository.save(newPost);

		return ResponseEntity.ok("success");
	}

	public ResponseEntity<?> modifyPost(String username, PostModifyRequestDto dto) {
		Post post = postRepository.findById(dto.getPostId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		if (!user.getId().equals(post.getUser().getId())) {
			return ResponseEntity.badRequest().body("해당 게시글에 대한 권한이 없습니다.");
		}

		if (dto.getTitle() != null) {
			post.modifyTitle(dto.getTitle());
		}
		if (dto.getBody() != null) {
			post.modifyBody(dto.getBody());
		}
		if (dto.getCategory() != null) {
			post.modifyCategory(dto.getCategory());
		}
		if (dto.getState() != null) {
			post.modifyState(dto.getState());
		}
		if (dto.getTechs() != null) {
			post.modifyTechs(dto.getTechs());
		}

		return ResponseEntity.ok("success");
	}

	public ResponseEntity<?> deletePost(String username, Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		if (!user.getId().equals(post.getUser().getId())) {
			return ResponseEntity.badRequest().body("해당 게시글에 대한 권한이 없습니다.");
		}

		postRepository.delete(post);

		return ResponseEntity.ok("success");
	}
}
