package project.como.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostState;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);
	Page<Post> findAllByCategoryOrderByCreatedDate(Category category, Pageable pageable);
	Page<Post> findAllByCategoryAndStateOrderByCreatedDate(Category category, PostState state, Pageable pageable);

}

