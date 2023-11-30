package project.como.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {
    private Long parentId;

    @NotBlank // null , "", " " / 공백 댓글은 의미없어서 NotBlank 사용
    private String body; // String이라 @



}
