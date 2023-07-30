package project.como.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import project.como.domain.post.model.Post;

import java.util.List;

@Data
@Builder
public class PostsResponseDto {
	private int totalPages;
	private long totalElements;
	private int currentPage;
	private List<PostDetailResponseDto> posts;
}
