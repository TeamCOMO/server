package project.como.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.interest.model.Interest;
import project.como.domain.interest.repository.InterestRepository;
import project.como.domain.post.dto.PostCreateRequestDto;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.dto.PostModifyRequestDto;
import project.como.domain.post.dto.PostsResponseDto;
import project.como.domain.post.exception.PostAccessDeniedException;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostState;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

	private final int TOTAL_ITEMS_PER_PAGE = 20;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final InterestRepository interestRepository;

	public void createPost(String username, PostCreateRequestDto dto) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		Post newPost = Post.builder()
				.title(dto.getTitle())
				.body(dto.getBody())
				.category(dto.getCategory())
				.state(PostState.Active)
				.techs(dto.getTechs())
				.user(user)
				.build();

		postRepository.save(newPost);
	}

	public void modifyPost(String username, PostModifyRequestDto dto) {
		Post post = postRepository.findById(dto.getPostId()).orElseThrow(() -> new PostNotFoundException(dto.getPostId()));
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		if (!user.getId().equals(post.getUser().getId()))
			throw new PostAccessDeniedException();

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
	}

	public void deletePost(String username, Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		if (!user.getId().equals(post.getUser().getId()))
			throw new PostAccessDeniedException();

		postRepository.delete(post);
	}

	public PostDetailResponseDto getDetailPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		post.countRead();

		return PostDetailResponseDto.builder()
				.title(post.getTitle())
				.body(post.getBody())
				.category(post.getCategory())
				.state(post.getState())
				.techs(post.getTechs())
				.build();
	}

	public PostsResponseDto getPostsByCategory(Pageable pageable, int pageNo, String category) {
		Page<Post> postPage = postRepository.findAllByCategoryOrderByCreatedDate(Category.valueOf(category), PageRequest.of(pageNo, TOTAL_ITEMS_PER_PAGE));

		return PostsResponseDto.builder()
				.totalPages(postPage.getTotalPages())
				.totalElements(postPage.getTotalElements())
				.currentPage(postPage.getNumber())
				.posts(postPage.getContent().stream().map((post) -> PostDetailResponseDto.builder()
						.title(post.getTitle())
						.body(post.getBody())
						.category(post.getCategory())
						.state(post.getState())
						.techs(post.getTechs())
						.build()).toList())
				.build();
	}


	/*public PostsResponseDto getInterestPostsByUser(Pageable pageable, int pageNo, String username){
		User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
		List<Interest> interests = interestRepository.findAllbyUser(user);

		//Page<Post> postPage = postRepository.findInterestPosts(interests, PageRequest.of(pageNo, TOTAL_ITEMS_PER_PAGE));
		List<Post> posts = interests.stream().map(i -> i.getPost()).collect(Collectors.toList());
		Page<Post> postPage = new PageImpl<>(posts, PageRequest.of(pageNo, TOTAL_ITEMS_PER_PAGE), posts.size());

		return PostsResponseDto.builder()
				.totalPages(postPage.getTotalPages())
				.totalElements(postPage.getTotalElements())
				.currentPage(postPage.getNumber())
				.posts(postPage.getContent().stream().map(post -> PostDetailResponseDto.builder()
						.title(post.getTitle())
						.body(post.getBody())
						.category(post.getCategory())
						.state(post.getState())
						.techs(post.getTechs())
						.build()).toList()
				).build();
	}*/
}
