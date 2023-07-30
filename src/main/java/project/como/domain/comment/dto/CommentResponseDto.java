package project.como.domain.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentResponseDto {
    List<CommentDetailDto> comments;
}
