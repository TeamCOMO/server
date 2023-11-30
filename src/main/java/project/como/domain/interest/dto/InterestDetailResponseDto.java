package project.como.domain.interest.dto;

import lombok.Builder;
import lombok.Data;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.PostState;
import project.como.domain.post.model.Tech;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class InterestDetailResponseDto {

    private Long interestId;
    private Long postId;
    private String title;
    private String body;
    private Category category;
    private PostState state;
    private List<String> techs;
    private Long heartCount;
    private Long readCount;
    private String createdDate;
}
