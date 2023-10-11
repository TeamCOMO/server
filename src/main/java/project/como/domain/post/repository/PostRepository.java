package project.como.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.PostState;
import project.como.domain.post.model.Tech;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Query("SELECT p FROM Post p WHERE (:stacks IS NULL OR :stacks MEMBER OF p.techs) ORDER BY p.createdDate DESC")
	Page<Post> findAllByOrderByCreatedDateDesc(List<Tech> stacks, Pageable pageable);

	@Query("SELECT p FROM Post p WHERE p.category = :category AND (:stacks IS NULL OR :stacks MEMBER OF p.techs) ORDER BY p.createdDate DESC")
	Page<Post> findAllByCategoryOrderByCreatedDateDesc(Category category, List<Tech> stacks, Pageable pageable);
	Page<Post> findAllByCategoryAndStateOrderByCreatedDate(Category category, PostState state, Pageable pageable);

}

