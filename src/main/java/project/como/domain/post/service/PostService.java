package project.como.domain.post.service;

import static java.time.format.DateTimeFormatter.*;

import jakarta.annotation.Nullable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.como.domain.apply.service.ApplyService;
import project.como.domain.comment.repository.CommentRepository;
import project.como.domain.image.model.Image;
import project.como.domain.image.repository.ImageRepository;
import project.como.domain.image.service.ImageService;
import project.como.domain.interest.repository.InterestRepository;
import project.como.domain.post.dto.PostCreateRequestDto;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.dto.PostModifyRequestDto;
import project.como.domain.post.dto.PostPagingResponseDto;
import project.como.domain.post.dto.PostsResponseDto;
import project.como.domain.post.exception.HeartConflictException;
import project.como.domain.post.exception.HeartNotFoundException;
import project.como.domain.post.exception.PostAccessDeniedException;
import project.como.domain.post.exception.PostImageCountExceededException;
import project.como.domain.post.exception.PostImageUrlNotFoundException;
import project.como.domain.post.exception.PostNotFoundException;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Heart;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostState;
import project.como.domain.post.model.PostTech;
import project.como.domain.post.model.Tech;
import project.como.domain.post.repository.HeartRepository;
import project.como.domain.post.repository.PostCustomRepository;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.post.repository.PostTechRepository;
import project.como.domain.post.repository.TechRepository;
import project.como.domain.user.exception.UserNotFoundException;
import project.como.domain.user.model.User;
import project.como.domain.user.repository.UserRepository;
import project.como.global.auth.exception.UnauthorizedException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private final int TOTAL_ITEMS_PER_PAGE = 12;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final PostCustomRepository postCustomRepository;
	private final InterestRepository interestRepository;
	private final HeartRepository heartRepository;
	private final CommentRepository commentRepository;
	private final ImageService imageService;
	private final ImageRepository imageRepository;
	private final TechRepository techRepository;
	private final PostTechRepository postTechRepository;
	private final ApplyService applyService;

	@Transactional
	public String create(String username, PostCreateRequestDto dto, @Nullable List<MultipartFile> images) {
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

		List<PostTech> mappingList = mappingPostAndTech(newPost, dto.getTechs());

		newPost.addTechs(mappingList);
		postTechRepository.saveAll(mappingList);

		if (images != null && !images.isEmpty()) imageService.uploadImages(username, newPost, images);

		return postRepository.save(newPost).getId().toString();
	}

	@Transactional
	public void modify(String username, PostModifyRequestDto dto, List<MultipartFile> images) {
		Post post = postRepository.findById(dto.getPostId()).orElseThrow(() -> new PostNotFoundException(dto.getPostId()));
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		List<String> urlList = imageService.findImagesByPostId(post.getId()).stream().map(Image::getUrl).toList();

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
			applyService.modifyStatesOfPost(post);
		}
		if (dto.getTechs() != null) {
			postTechRepository.deleteAllByPostId(post.getId());
			postTechRepository.saveAll(mappingPostAndTech(post, dto.getTechs()));
		}

		if (dto.getOldUrls() != null) {
			for (String oldUrl : dto.getOldUrls())
				if (!urlList.contains(oldUrl)) throw new PostImageUrlNotFoundException(oldUrl);
		}

		int oldSize = 0;
		if (dto.getOldUrls() != null) oldSize = dto.getOldUrls().size();

		int imgSize = 0;
		if (images != null) imgSize = images.size();

		if (imgSize + urlList.size() - oldSize > 5) throw new PostImageCountExceededException();

		if (images != null) imageService.uploadImages(username, post, images);
		if (dto.getOldUrls() != null) imageService.deleteImages(dto.getOldUrls());
	}

	@Transactional
	public void deleteById(String username, Long postId) {
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

	public PostDetailResponseDto getById(Long postId, String username) {
		PostDetailResponseDto dto = postCustomRepository.findPostDetailById(postId, username);

		return dto;
	}

	public PostsResponseDto getByCategory(int pageNo, String category, List<String> stacks) {
		Page<PostPagingResponseDto> postPage = postCustomRepository.findAllByCategoryAndTechs(category != null ? Category.valueOf(category) : null, stacks, PageRequest.of(pageNo, TOTAL_ITEMS_PER_PAGE));

		return PostsResponseDto.builder()
				.totalPages(postPage.getTotalPages())
				.totalElements(postPage.getTotalElements())
				.currentPage(postPage.getNumber())
				.posts(postPage.getContent())
				.build();
	}

	public PostsResponseDto getByMyself(String username, int pageNo) {
		User user = userRepository.findByUsername(username).orElseThrow(UnauthorizedException::new);

		Page<Post> postPage = postRepository.findAllByUserOrderByCreatedDateDesc(user, PageRequest.of(pageNo, TOTAL_ITEMS_PER_PAGE));

		return PostsResponseDto.builder()
				.totalPages(postPage.getTotalPages())
				.totalElements(postPage.getTotalElements())
				.currentPage(postPage.getNumber())
				.posts(postPage.stream().map(p -> PostPagingResponseDto.builder()
						.id(p.getId())
						.nickname(user.getNickname())
						.title(p.getTitle())
						.category(p.getCategory())
						.state(p.getState())
						.techs(p.getTechList().stream().distinct().map(pt -> pt.getTech().getStack()).toList())
						.heartCount(p.getHeartCount())
						.createdDate(p.getCreatedDate().format(ISO_LOCAL_DATE))
						.readCount(p.getReadCount())
						.build()).toList())
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

	private List<PostTech> mappingPostAndTech(Post post, List<String> techList) {
		List<PostTech> mappingList = new ArrayList<>();

		for (String stack : techList) {
			Optional<Tech> tech = techRepository.findByStack(stack);
			if (tech.isEmpty()) {
				tech = Optional.of(techRepository.save(Tech.builder().stack(stack).build()));
			}
			// 처음에 DB에 주입해줘도 될 거 같은 로직


			mappingList.add(PostTech.builder()
					.post(post)
					.tech(tech.get())
					.build());
		}

		return mappingList;
	}
}
