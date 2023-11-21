package project.como.domain.interest.service;

import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.interest.dto.InterestCreateRequestDto;
import project.como.domain.interest.dto.InterestDetailResponseDto;
import project.como.domain.interest.dto.InterestResponseDto;
import project.como.domain.interest.exception.AlreadyInterestException;
import project.como.domain.interest.exception.InterestNotFoundException;
import project.como.domain.interest.model.Interest;
import project.como.domain.interest.repository.InterestRepository;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostTech;
import project.como.domain.post.model.Tech;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

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
            throw new AlreadyInterestException(findUser.getId(), findPost.getId());
        }
        Interest interest = dto.toEntity(findUser, findPost);
        interestRepository.save(interest);
    }

    public InterestResponseDto findInterests(String username, int pageNo){
        User findUser = userRepository.findByUsername(username).orElseThrow(() ->new UserNotFoundException());
        List<Interest> interests = interestRepository.findAllByUser(findUser);
        Page<Interest> interestPostPage = new PageImpl<>(interests
                , PageRequest.of(pageNo, TOTAL_ITEMS_PER_PAGE), interests.size());


        return InterestResponseDto.builder()
                .totalPages(interestPostPage.getTotalPages())
                .totalElements(interestPostPage.getTotalElements())
                .currentPage(pageNo)
                .interests(interestPostPage.getContent().stream()
                        .map(i -> InterestDetailResponseDto.builder()
                                .interestId(i.getId())
                                .postId(i.getPost().getId())
                                .title(i.getPost().getTitle())
                                .body(i.getPost().getBody())
                                .category(i.getPost().getCategory())
                                .state(i.getPost().getState())
                                .techs(i.getPost().getTechList().stream()
                                        .map(postTech -> postTech.getTech().getStack())
                                        .collect(Collectors.toList()))
                                .heartCount(i.getPost().getHeartCount())
                                .readCount(i.getPost().getReadCount())
                                .createdDate(i.getPost().getCreatedDate().format(ISO_LOCAL_DATE)) // 추가로 시:분은 표현된는게 낫지 않을까?
                                .build())
                        .sorted(Comparator.comparing(InterestDetailResponseDto::getCreatedDate))
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

    private List<Tech> getTechList(List<PostTech> techList) {
        List<Tech> techs = new ArrayList<>();
        for (PostTech pt : techList) {
            techs.add(pt.getTech());
        }
        return techs;
    }
}
