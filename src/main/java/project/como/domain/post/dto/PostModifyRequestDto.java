package project.como.domain.post.dto;

import lombok.Data;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.PostState;
import project.como.domain.post.model.Tech;

import java.util.List;

@Data
public class PostModifyRequestDto {
	private Long postId;
	private String title;
	private String body;
	private Category category;
	private PostState state;
	private List<Tech> techs;
}
