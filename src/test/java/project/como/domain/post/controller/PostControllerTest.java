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
import project.como.domain.post.model.Tech;
import project.como.domain.post.repository.PostRepository;
import project.como.domain.post.service.PostService;
import project.como.domain.user.model.User;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostControllerTest {

	@Autowired
	PostService postService;

	@Autowired
	PostRepository postRepository;

	@PersistenceContext
	EntityManager em;

	@Test
	@Transactional
	@DisplayName("게시글 생성")
	void createPost() {
		User user = em.find(User.class, 1);
		PostCreateRequestDto dto = new PostCreateRequestDto();

		dto.setBody("test body");
		dto.setCategory(Category.Study);
		dto.setTitle("test title");
		ArrayList<Tech> techList = new ArrayList<>();
		techList.add(Tech.Java);
		techList.add(Tech.Spring);
		dto.setTechs(techList);

		postService.createPost(user.getUsername(), dto);

		Post post = em.find(Post.class, 1);

		assertThat(post.getTitle()).isEqualTo("test title");
		assertThat(post.getBody()).isEqualTo("test body");
		assertThat(post.getTechs()).containsExactly(Tech.Java, Tech.Spring);
	}

	@Test
	@DisplayName("게시물 단건 조회")
	void getDetailPost() {
		final Long POST_ID = 1L;

		Post findPost = em.find(Post.class, POST_ID);

		PostDetailResponseDto servicePostDto = postService.getDetailPost(POST_ID);

		assertThat(findPost.getTitle()).isEqualTo(servicePostDto.getTitle());
		assertThat(findPost.getBody()).isEqualTo(servicePostDto.getBody());
		assertThat(findPost.getCategory()).isEqualTo(servicePostDto.getCategory());
		assertThat(findPost.getTechs()).containsExactly(Tech.Java, Tech.Spring);
		assertThat(servicePostDto.getTechs()).containsExactly(Tech.Java, Tech.Spring);
	}

	@Test
	@DisplayName("게시물 카테고리별 조회")
	void getPostsByCategory() {
		final String CATEGORY = "Study";
		Pageable pageable = PageRequest.of(0, 5);

		PostsResponseDto dto = postService.getPostsByCategory(pageable, 0, CATEGORY);

		assertThat(dto.getTotalElements()).isEqualTo(3);
		assertThat(dto.getPosts().get(0).getTitle()).isEqualTo("test title");
	}

	@Test
	@Transactional
	@DisplayName("게시물 수정")
	void modifyPost() {
		final String USERNAME = "test";
		PostModifyRequestDto dto = new PostModifyRequestDto();

		Post findPost = em.find(Post.class, 1);
		assertThat(findPost.getTitle()).isEqualTo("test title");
		assertThat(findPost.getBody()).isEqualTo("test body");

		dto.setPostId(1L);
		dto.setTitle("change test");
		dto.setBody("change body");

		postService.modifyPost(USERNAME, dto);

		Post changedPost = em.find(Post.class, 1);

		assertThat(changedPost.getTitle()).isEqualTo("change test");
		assertThat(changedPost.getBody()).isEqualTo("change body");
	}

	@Test
	@Transactional
	@DisplayName("게시물 삭제")
	void deletePost() {
		final String USERNAME = "test";
		final Long POST_ID = 1L;

		int initSize = postRepository.findAll().size();

		postService.deletePost(USERNAME, POST_ID);

		int sizeAfterRemoval = postRepository.findAll().size();

		assertThat(initSize).isEqualTo(sizeAfterRemoval + 1);
	}

	@Test
	void makeHeart() {
	}

	@Test
	void deleteHeart() {
	}
}