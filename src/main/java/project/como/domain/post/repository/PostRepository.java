package project.como.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostState;

public interface PostRepository extends JpaRepository<Post, Long> {
//	@Query(value = "SELECT * FROM POST p ORDER BY p.created_date DESC")
	Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);
	Page<Post> findAllByCategoryOrderByCreatedDateDesc(Category category, Pageable pageable);
	Page<Post> findAllByCategoryAndStateOrderByCreatedDate(Category category, PostState state, Pageable pageable);

}

