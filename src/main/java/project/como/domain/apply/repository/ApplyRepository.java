package project.como.domain.apply.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.como.domain.apply.model.Apply;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
	Optional<Apply> findApplyByUserAndPost(User user, Post post);

	List<Apply> findAllByPost(Post post);

	Optional<Apply> findByUser(User user);

	@Query(value = "SELECT a FROM Apply a JOIN FETCH a.post WHERE a.post = :post ORDER BY a.post.createdDate DESC",
			countQuery = "SELECT COUNT(a) FROM Apply a WHERE a.post = :post")
	Page<Apply> findAppliesByPost(@Param("post") Post post, Pageable pageable);
  
	@Query(value = "SELECT a FROM Apply a JOIN FETCH a.post WHERE a.user = :user ORDER BY a.post.createdDate DESC",
			countQuery = "SELECT COUNT(a) FROM Apply a WHERE a.user = :user")
	Page<Apply> findAllByUser(@Param("user") User user, Pageable pageable);
}
