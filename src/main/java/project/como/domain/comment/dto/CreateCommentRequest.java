package project.como.domain.comment.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import project.como.domain.comment.model.Comment;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data

public class CreateCommentRequest {
    private Long id;
    private String body;
    private User user;
    private Post post;

    public Comment toEntity(User user, Post post) {
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .body(body)
                .build();
        return comment;
    }

}
