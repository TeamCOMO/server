package project.como.domain.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.PostState;
import project.como.domain.post.model.Tech;

import java.util.List;


@Data
@Builder
public class PostDetailResponseDto {
	private Long id;
	private String title;
	private String body;
	private Category category;
	private PostState state;
	private List<Tech> techs;
	private List<String> images;
	private Long heartCount;

	public PostDetailResponseDto() {
	}

	@QueryProjection
	public PostDetailResponseDto(Long id, String title, String body, Category category, PostState state, List<Tech> techs, List<String> images, Long heartCount) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.category = category;
		this.state = state;
		this.techs = null;
		this.images = images;
		this.heartCount = heartCount;
	}
}
