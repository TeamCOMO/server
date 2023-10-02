package project.como.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.dto.PostPagingResponseDto;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.Tech;

import java.util.List;

public interface PostCustomRepository {

	Page<PostDetailResponseDto> findAllByCategoryAndTechs(Category category, List<Tech> techs, Pageable pageable);
}
