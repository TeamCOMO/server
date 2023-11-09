package project.como.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import project.como.domain.post.model.Category;

@Data
public class PostCreateRequestDto {
	@NotBlank(message = "제목을 입력해주세요.")
	private String title;
	@NotBlank(message = "본문을 입력해주세요.")
	private String body;
	@NotNull(message = "카테고리를 선택해주세요.")
	private Category category;
	@NotNull(message = "기술스택을 선택해주세요.")
	private List<String> techs;
}
