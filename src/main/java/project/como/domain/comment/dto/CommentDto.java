package project.como.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.como.domain.comment.model.Comment;

@Data
@AllArgsConstructor
public class CommentDto {

    private String body;
    public CommentDto(Comment comment){
        this.body = comment.getBody();
    }
}
