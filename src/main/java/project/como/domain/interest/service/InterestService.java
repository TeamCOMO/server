package project.como.domain.interest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.interest.dto.InterestCreateRequestDto;
import project.como.domain.interest.dto.InterestDetailResponseDto;
import project.como.domain.interest.dto.InterestResponseDto;
import project.como.domain.interest.exception.InterestNotFoundException;
import project.como.domain.interest.model.Interest;
import project.como.domain.interest.repository.InterestRepository;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.domain.post.model.Post;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterestService {
    private final int TOTAL_ITEMS_PER_PAGE = 20;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final InterestRepository interestRepository;

    @Transactional
    public void createInterest(String username, InterestCreateRequestDto dto){
        User findUser = userRepository.findByUsername(username).orElseThrow(() ->new UserNotFoundException());
        Post findPost = postRepository.findById(dto.getPostId()).orElseThrow(() -> new PostNotFoundException(dto.getPostId()));

        if (isInterestAlreadyRegistered(findUser, findPost)) {
            throw new IllegalStateException("이미 관심 등록한 게시물입니다.");
        }
        Interest interest = dto.toEntity(findUser, findPost);
        interestRepository.save(interest);
    }

    public InterestResponseDto findInterests(String username, int pageNo, Pageable pageable){
        User findUser = userRepository.findByUsername(username).orElseThrow(() ->new UserNotFoundException());
        List<Interest> interests = interestRepository.findAllByUser(findUser);
        Page<Interest> interestPostPage = new PageImpl<>(interests, PageRequest.of(pageNo, TOTAL_ITEMS_PER_PAGE), interests.size());

        return InterestResponseDto.builder()
                .totalPages(interestPostPage.getTotalPages())
                .totalElements(interestPostPage.getTotalElements())
                .currentPage(pageNo)
                .interests(interestPostPage.getContent().stream()
                        .map(i -> InterestDetailResponseDto.builder()
                                .title(i.getPost().getTitle())
                                .body(i.getPost().getBody())
                                .category(i.getPost().getCategory())
                                .state(i.getPost().getState())
                                .techs(i.getPost().getTechs())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
    @Transactional
    public void deleteInterest(Long interestId, String username){
        User findUser = userRepository.findByUsername(username).orElseThrow(() ->new UserNotFoundException());
        Interest findInterest = interestRepository.findById(interestId).orElseThrow(() -> new InterestNotFoundException(interestId));
        // interest에 대한 예외 처리로 변경 필요

        interestRepository.delete(findInterest);
    }

    // 해당 유저가 이미 해당 게시물에 관심 등록을 했는지 체크
    private boolean isInterestAlreadyRegistered(User user, Post post) {
        return interestRepository.existsByUserAndPost(user, post);
    }

}
