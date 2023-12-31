package project.como.domain.post.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.post.dto.PostCreateRequestDto;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.dto.PostModifyRequestDto;
import project.como.domain.post.dto.PostsResponseDto;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostTech;
import project.como.domain.post.model.Tech;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.post.service.PostService;
import project.como.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostControllerTest {

	@Autowired
	PostService postService;

	@Autowired
	PostRepository postRepository;

	@PersistenceContext
	EntityManager em;

	static final String USERNAME = "test";

	@Test
	@Transactional
	@DisplayName("게시글 생성")
	void createPost() {
		User user = em.find(User.class, 1);
		PostCreateRequestDto dto = new PostCreateRequestDto();

		dto.setBody("test body");
		dto.setCategory(Category.Study);
		dto.setTitle("test title");
		ArrayList<String> techList = new ArrayList<>();
		techList.add("Java");
		techList.add("Spring");
		dto.setTechs(techList);

		postService.create(user.getUsername(), dto, null);

		Post post = em.find(Post.class, 1);

		assertThat(post.getTitle()).isEqualTo("test title");
		assertThat(post.getBody()).isEqualTo("test body");
		assertThat(post.getTechList())
				.allSatisfy(postTech -> {
					assertThat(postTech.getTech())
							.isIn("Java", "Spring");
				});
	}

	@Test
	@DisplayName("게시물 단건 조회")
	void getDetailPost() {
		final Long POST_ID = 1L;
		final String USERNAME = "smc9919";

		Post findPost = em.find(Post.class, POST_ID);

		PostDetailResponseDto servicePostDto = postService.getById(POST_ID, USERNAME);

		assertThat(findPost.getTitle()).isEqualTo(servicePostDto.getTitle());
		assertThat(findPost.getBody()).isEqualTo(servicePostDto.getBody());
		assertThat(findPost.getCategory()).isEqualTo(servicePostDto.getCategory());
		assertThat(findPost.getTechList())
				.allSatisfy(postTech -> {
					assertThat(postTech.getTech())
							.isIn("Java", "Spring");
				});
	}

	@Test
	@DisplayName("게시물 카테고리별 조회")
	void getPostsByCategory() {
		final String CATEGORY = "Study";
		final List<String> STACKS = List.of("Java", "Spring");
		Pageable pageable = PageRequest.of(0, 5);

		PostsResponseDto dto = postService.getByCategory(0, CATEGORY, STACKS);

		assertThat(dto.getTotalElements()).isEqualTo(3);
		assertThat(dto.getPosts().get(0).getTitle()).isEqualTo("test title");
	}

	@Test
	@Transactional
	@DisplayName("게시물 수정")
	void modifyPost() {
		PostModifyRequestDto dto = new PostModifyRequestDto();

		Post findPost = em.find(Post.class, 1);
		assertThat(findPost.getTitle()).isEqualTo("test title");
		assertThat(findPost.getBody()).isEqualTo("test body");

		dto.setPostId(1L);
		dto.setTitle("change test");
		dto.setBody("change body");

		postService.modify(USERNAME, dto, null);

		Post changedPost = em.find(Post.class, 1);

		assertThat(changedPost.getTitle()).isEqualTo("change test");
		assertThat(changedPost.getBody()).isEqualTo("change body");
	}

	@Test
	@Transactional
	@DisplayName("게시물 삭제")
	void deletePost() {
		final Long POST_ID = 1L;

		int initSize = postRepository.findAll().size();

		postService.deleteById(USERNAME, POST_ID);

		int sizeAfterRemoval = postRepository.findAll().size();

		assertThat(initSize).isEqualTo(sizeAfterRemoval + 1);
	}

	@Test
	@Transactional
	@DisplayName("게시물 하트")
	void makeHeart() {
		final Long POST_ID = 2L;

		Post findPost = em.find(Post.class, 2);
		Long initCount = findPost.getHeartCount();

		postService.makeHeart(USERNAME, POST_ID);

		Post postAfterHeart = em.find(Post.class, 2);
		Long CountAfterHeart = postAfterHeart.getHeartCount();

		assertThat(initCount).isEqualTo(CountAfterHeart - 1);
	}

	@Test
	@Transactional
	@DisplayName("게시물 하트 취소")
	void deleteHeart() {
		final Long POST_ID = 2L;

		postService.makeHeart(USERNAME, POST_ID);
		Post findPost = em.find(Post.class, POST_ID);
		Long countBeforeRemoval = findPost.getHeartCount();

		postService.deleteHeart(USERNAME, POST_ID);
		Post postAfterRemoval = em.find(Post.class, POST_ID);
		Long countAfterRemoval = postAfterRemoval.getHeartCount() + 1L;

		assertThat(countBeforeRemoval).isEqualTo(countAfterRemoval);
	}
}