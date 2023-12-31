package project.como.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.PostState;

import java.util.List;

@Data
public class PostModifyRequestDto {
	private Long postId;
	private String title;
	private String body;
	private Category category;
	private PostState state;
	private List<String> techs;
	List<@NotBlank @URL String> oldUrls;
}
