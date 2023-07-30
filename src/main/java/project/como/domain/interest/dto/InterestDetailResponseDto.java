package project.como.domain.interest.dto;

import lombok.Builder;
import lombok.Data;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.PostState;
import project.como.domain.post.model.Tech;

import java.util.List;

@Data
@Builder
public class InterestDetailResponseDto {

    private String title;
    private String body;
    private Category category;
    private PostState state;
    private List<Tech> techs;
}
