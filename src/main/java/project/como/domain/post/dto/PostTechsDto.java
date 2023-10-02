package project.como.domain.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.como.domain.post.model.Tech;

import java.util.List;

@Data
@NoArgsConstructor
public class PostTechsDto {
	private Long postId;
	private List<Tech> techs;

	@QueryProjection
	public PostTechsDto(Long postId, List<Tech> techs) {
		this.postId = postId;
		this.techs = techs;
	}
}
