package project.como.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.PostState;
import project.como.domain.post.model.Tech;

import java.util.List;

@Data
public class PostCreateRequestDto {
	@NotBlank
	private String title;
	@NotBlank
	private String body;
	@NotNull
	private Category category;
	@NotNull
	private List<Tech> techs;
}
