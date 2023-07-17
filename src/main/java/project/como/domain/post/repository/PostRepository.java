package project.como.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.como.domain.post.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
