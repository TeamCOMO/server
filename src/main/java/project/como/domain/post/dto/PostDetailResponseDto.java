package project.como.domain.post.dto;

import lombok.*;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.PostState;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponseDto {
	private Long id;
	private String createdDate;
	private String title;
	private String body;
	private Category category;
	private PostState state;
	private List<String> techs;
	private List<String> images;
	private Long heartCount;
	private Long readCount;

//	public PostDetailResponseDto() {
//	}
//
//	@QueryProjection
//	public PostDetailResponseDto(Long id, String title, String body, Category category, PostState state, Long heartCount) {
//		this.id = id;
//		this.title = title;
//		this.body = body;
//		this.category = category;
//		this.state = state;
//		this.heartCount = heartCount;
//	}
}
