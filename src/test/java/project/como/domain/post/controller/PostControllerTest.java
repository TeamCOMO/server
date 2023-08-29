package project.como.domain.post.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.post.dto.PostCreateRequestDto;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.Tech;
import project.como.domain.post.service.PostService;
import project.como.domain.user.model.User;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostControllerTest {

	@Autowired
	PostService postService;

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
	void getDetailPost() {
	}

	@Test
	void getPostsByCategory() {
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