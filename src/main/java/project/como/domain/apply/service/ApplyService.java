package project.como.domain.apply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.apply.model.Apply;
import project.como.domain.apply.repository.ApplyRepository;
import project.como.domain.post.exception.PostInactiveException;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostState;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.exception.UserInfoNotFoundException;
import project.como.domain.user.exception.UserNotEligibleForApplyException;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
	private final ApplyRepository applyRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	public void makeApply(String username, Long postId) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

		if (user.equals(post.getUser()))
			throw new UserNotEligibleForApplyException();
		if (user.getBlogUrl() == null && user.getGithubUrl() == null)
			throw new UserInfoNotFoundException();
		if (post.getState().equals(PostState.Inactive))
			throw new PostInactiveException();

		Apply application = Apply.builder()
				.user(user)
				.post(post)
				.build();

		applyRepository.save(application);
	}

	public boolean getApply(String username, Long postId) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

		return applyRepository.findApplyByUserAndPost(user, post).isPresent();
	}
}
