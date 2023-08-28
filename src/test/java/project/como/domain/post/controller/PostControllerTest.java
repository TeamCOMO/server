package project.como.domain.post.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.como.domain.post.dto.PostCreateRequestDto;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.dto.PostsResponseDto;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.Tech;
import project.como.domain.post.service.PostService;
import project.como.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostControllerTest {

	@Autowired
	PostService postService;

	@PersistenceContext
	EntityManager em;

	@Test
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
	void getPostsByCategory() {
		final String CATEGORY = "Study";
		Pageable pageable = PageRequest.of(0, 5);

		PostsResponseDto dto = postService.getPostsByCategory(pageable, 0, CATEGORY);

		assertThat(dto.getTotalElements()).isEqualTo(3);
		assertThat(dto.getPosts().get(0).getTitle()).isEqualTo("test title");
	}

	@Test
	void modifyPost() {
	}

	@Test
	void deletePost() {
	}

	@Test
	void makeHeart() {
	}

	@Test
	void deleteHeart() {
	}
}