package project.como.domain.heart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.heart.exception.HeartConflictException;
import project.como.domain.heart.exception.HeartNotFoundException;
import project.como.domain.heart.model.Heart;
import project.como.domain.heart.repository.HeartRepository;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.domain.post.model.Post;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final HeartRepository heartRepository;

	public void makeHeart(String username, Long postId) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

		if (heartRepository.findByPostAndUser(post, user).isPresent())
			throw new HeartConflictException();

		Heart heart = Heart.builder()
				.user(user)
				.post(post)
				.build();

		heartRepository.save(heart);
	}

	public void deleteHeart(String username, Long postId) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

		Heart heart = heartRepository.findByPostAndUser(post, user).orElseThrow(HeartNotFoundException::new);

		heartRepository.delete(heart);
	}
}
