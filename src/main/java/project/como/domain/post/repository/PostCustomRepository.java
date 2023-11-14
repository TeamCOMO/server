package project.como.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.dto.PostPagingResponseDto;
import project.como.domain.post.model.Category;

import java.util.List;

public interface PostCustomRepository {

	Page<PostPagingResponseDto> findAllByCategoryAndTechs(Category category, List<String> stacks, Pageable pageable);
	PostDetailResponseDto findPostDetailById(Long id, String username);
}
