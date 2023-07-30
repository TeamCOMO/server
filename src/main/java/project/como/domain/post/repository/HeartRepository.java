package project.como.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.como.domain.post.model.Heart;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

	Optional<Heart> findByPostAndUser(Post post, User user);
}
