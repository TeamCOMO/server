package project.como.domain.comment.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import project.como.domain.comment.model.Comment;
import project.como.domain.user.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.post p where p.id = :postId")
    public List<Comment> findAllByPostId(@Param("postId") Long postId);
    
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);

    @Query("select c from Comment c join fetch c.post p where c.user = :user " +
            "group by p order by c.createdDate")
    List<Comment> findAllByUser(@Param("user") User user, Pageable pageable);
}