package project.como.domain.comment.reposiory;

import org.springframework.data.jpa.repository.JpaRepository;
import project.como.domain.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
