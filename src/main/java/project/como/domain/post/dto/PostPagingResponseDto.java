package project.como.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.PostState;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPagingResponseDto {
	private Long id;
	private String title;
	private Category category;
	private PostState state;
	private List<String> techs;
	private Long heartCount;
}
