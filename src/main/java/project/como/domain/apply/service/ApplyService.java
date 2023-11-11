package project.como.domain.apply.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.apply.dto.ApplyListResponseDto;
import project.como.domain.apply.dto.ApplyStateModifyRequestDto;
import project.como.domain.apply.dto.ApplyUserResponseDto;
import project.como.domain.apply.exception.ApplyNotFoundException;
import project.como.domain.apply.exception.DuplicatedApplyException;
import project.como.domain.apply.model.Apply;
import project.como.domain.apply.model.ApplyState;
import project.como.domain.apply.repository.ApplyRepository;
import project.como.domain.post.exception.PostAccessDeniedException;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplyService {
	private final ApplyRepository applyRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Transactional
	public String create(String username, Long postId) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

		if (user.equals(post.getUser()))
			throw new UserNotEligibleForApplyException();
		if (user.getBlogUrl() == null && user.getGithubUrl() == null)
			throw new UserInfoNotFoundException();
		if (post.getState().equals(PostState.Inactive))
			throw new PostInactiveException();
		if (applyRepository.findApplyByUserAndPost(user, post).isPresent())
			throw new DuplicatedApplyException();

		applyRepository.save(
				Apply.builder()
				.user(user)
				.post(post)
				.state(ApplyState.SUBMIT)
				.build());

		return post.getId().toString();
	}

	public ApplyState check(String username, Long postId) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		Optional<Apply> apply = applyRepository.findApplyByUserAndPost(user, post);

        return apply.map(Apply::getState).orElse(null);
    }

	public ApplyListResponseDto getAllByWriter(String username, Long postId) {
		User writer = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

		if (isNotPostWriter(post.getUser().getId(), writer.getId())) throw new PostAccessDeniedException();

		List<ApplyUserResponseDto> appliesInfo = new ArrayList<>();
		for (Apply apply : applyRepository.findAllByPost(post)) {
			User user = apply.getUser();
			List<String> portfolio = new ArrayList<>();
			portfolio.add(user.getBlogUrl());
			portfolio.add(user.getGithubUrl());
			appliesInfo.add(ApplyUserResponseDto.of(user.getUsername(), user.getEmail(), portfolio, apply.getState()));
		}

		return ApplyListResponseDto.of(appliesInfo);
	}

	@Transactional
	public void modifyState(String username, Long postId, ApplyStateModifyRequestDto dto) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

		if (isNotPostWriter(post.getUser().getId(), user.getId())) throw new PostAccessDeniedException();

		User applicant = userRepository.findByUsername(dto.applicantName()).orElseThrow(UserNotFoundException::new);
		Apply apply = applyRepository.findApplyByUserAndPost(applicant, post).orElseThrow(ApplyNotFoundException::new);
		apply.modifyState(dto.state());
	}

	@Transactional
	public void modifyStatesOfPost(Post post) {
		applyRepository.findAllByPost(post).forEach((apply) -> {
			apply.modifyState(ApplyState.ON_PROGRESS);
		});
	}

	private boolean isNotPostWriter(Long ownerId, Long requesterId) {
		return !ownerId.equals(requesterId);
	}
}
