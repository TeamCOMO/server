package project.como.domain.post.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.como.domain.comment.repository.CommentRepository;
import project.como.domain.image.model.Image;
import project.como.domain.image.repository.ImageRepository;
import project.como.domain.image.service.ImageService;
import project.como.domain.interest.repository.InterestRepository;
import project.como.domain.post.dto.*;
import project.como.domain.post.exception.*;
import project.como.domain.post.model.*;
import project.como.domain.post.repository.HeartRepository;
import project.como.domain.post.repository.PostCustomRepositoryImpl;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.post.repository.TechRepository;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final int TOTAL_ITEMS_PER_PAGE = 20;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final PostCustomRepositoryImpl postRepositoryImpl;
	private final InterestRepository interestRepository;
	private final HeartRepository heartRepository;
	private final CommentRepository commentRepository;
	private final ImageService imageService;
	private final ImageRepository imageRepository;
	private final TechRepository techRepository;

	public void createPost(String username, PostCreateRequestDto dto, @Nullable List<MultipartFile> images) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

		Post newPost = Post.builder()
				.title(dto.getTitle())
				.body(dto.getBody())
				.category(dto.getCategory())
				.state(PostState.Active)
				.user(user)
				.readCount(0L)
				.heartCount(0L)
				.build();

		List<Tech> techList = new LinkedList<>();
		for (String tech : dto.getTechs()) {
			techList.add(Tech.builder()
					.post(newPost)
					.stack(tech)
					.build());
		}

		if (images != null && !images.isEmpty()) imageService.uploadImages(username, newPost, images);

		postRepository.save(newPost);
		techRepository.saveAll(techList);
	}

	public void modifyPost(String username, PostModifyRequestDto dto, List<MultipartFile> images) {
		Post post = postRepository.findById(dto.getPostId()).orElseThrow(() -> new PostNotFoundException(dto.getPostId()));
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		List<Image> imageList = imageService.findImagesByPostId(post.getId());

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
			techRepository.deleteAllByPostId(post.getId());
			techRepository.saveAll(dto.getTechs().stream().map(tech -> Tech.builder()
					.post(post)
					.stack(tech)
					.build()).toList());
		}

		if (dto.getOldUrls() != null) {
			for (String oldUrl : dto.getOldUrls())
				if (!post.getImages().contains(oldUrl)) throw new PostImageUrlNotFoundException(oldUrl);
		}

		int oldSize = 0;
		if (dto.getOldUrls() != null) oldSize = dto.getOldUrls().size();

		if (images.size() + imageList.size() - oldSize > 5) throw new PostImageCountExceededException();

		if (images != null) imageService.uploadImages(username, post, images);
		if (dto.getOldUrls() != null) imageService.deleteImages(dto.getOldUrls());
	}

	public void deletePost(String username, Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

		if (!user.getId().equals(post.getUser().getId()))
			throw new PostAccessDeniedException();

		imageService.deleteImages(imageRepository.findAllByPostId(postId).stream().map(Image::getUrl).toList());

		interestRepository.deleteAllByPostId(postId);
		heartRepository.deleteAllByPostId(postId);
		commentRepository.deleteAllByPostId(postId);
		postRepository.delete(post);
	}

	public PostDetailResponseDto getDetailPost(Long postId) {
		PostDetailResponseDto dto = postRepositoryImpl.findPostDetailById(postId);

		log.info("dto : {}", dto);
		return dto;
	}

	public PostsResponseDto getPostsByCategory(int pageNo, String category, List<String> stacks) {
		Page<PostPagingResponseDto> postPage = postRepositoryImpl.findAllByCategoryAndTechs(Category.valueOf(category), stacks, PageRequest.of(pageNo, TOTAL_ITEMS_PER_PAGE));

		return PostsResponseDto.builder()
				.totalPages(postPage.getTotalPages())
				.totalElements(postPage.getTotalElements())
				.currentPage(postPage.getNumber())
				.posts(postPage.getContent())
				.build();
	}

	@Transactional
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
		post.countHeart();
	}

	@Transactional
	public void deleteHeart(String username, Long postId) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

		Heart heart = heartRepository.findByPostAndUser(post, user).orElseThrow(HeartNotFoundException::new);

		heartRepository.delete(heart);
		post.discountHeart();
	}

}
