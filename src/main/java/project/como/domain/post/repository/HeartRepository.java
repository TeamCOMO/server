package project.como.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.como.domain.post.model.Heart;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
	Optional<Heart> findByPostAndUser(Post post, User user);

	@Modifying
	@Query("DELETE FROM Heart h WHERE h.post.id = :postId")
	void deleteAllByPostId(@Param("postId") Long postId);
}
