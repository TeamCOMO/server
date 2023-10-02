package project.como.domain.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.PostState;
import project.como.domain.post.model.Tech;

import java.util.List;

@Data
@NoArgsConstructor
public class PostPagingResponseDto {
	private Long id;
	private String title;
	private String body;
	private Category category;
	private PostState state;
	private List<Tech> techs;
	private List<String> images;
	private Long heartCount;

	@QueryProjection
	public PostPagingResponseDto(Long id, String title, String body, Category category, PostState state, List<Tech> techs, List<String> images, Long heartCount) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.category = category;
		this.state = state;
		this.techs = techs;
		this.images = images;
		this.heartCount = heartCount;
	}
}
