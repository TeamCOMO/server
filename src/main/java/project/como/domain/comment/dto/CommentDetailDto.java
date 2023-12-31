package project.como.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class  CommentDetailDto {

    private Long id;

    private Long parentId;

    @NotBlank
    private String body;

    private String nickname;

    private String createdTime;

    private List<CommentDetailDto> children;


}
/**
 * 현재는 기능이 단순하기에 생성, 수정, 단일 조회 dto의 클래스 형식이 유사함.
 * 그래서 CommentDto 클래스로 통일. 생성만 toEntity()를 위해 살려두고 생성을 제외한 것은 CommentDto로 사용.
 */
