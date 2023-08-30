package project.como.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import project.como.domain.comment.model.Comment;
import project.como.domain.post.model.Post;
import project.como.domain.user.model.User;

@Data
public class CommentCreateRequestDto {
    @NotBlank // null , "", " " / 공백 댓글은 의미없어서 NotBlank 사용
    private String body; // String이라 @

    public Comment toEntity(User user, Post post) {
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .body(this.body)
                .build();
        return comment;
    }

}
