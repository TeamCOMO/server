package project.como.domain.apply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.apply.repository.ApplyRepository;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.repository.UserRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
	private final ApplyRepository applyRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
}
