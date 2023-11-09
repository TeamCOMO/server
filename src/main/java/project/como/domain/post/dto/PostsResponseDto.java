package project.como.domain.post.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostsResponseDto {
	private int totalPages;
	private long totalElements;
	private int currentPage;
	private List<PostPagingResponseDto> posts;
}
