package project.como.domain.comment.dto;

import lombok.*;

@Data @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentCreateResponseDto {
    private Long id;
}
