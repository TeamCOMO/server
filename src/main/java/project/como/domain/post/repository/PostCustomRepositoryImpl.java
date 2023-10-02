package project.como.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import project.como.domain.post.dto.*;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.Tech;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static project.como.domain.post.model.QPost.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
	private final JPAQueryFactory queryFactory;

	public Page<PostDetailResponseDto> findAllByCategoryAndTechs(Category category, List<Tech> techs, Pageable pageable) {

		List<PostDetailResponseDto> content = queryFactory.select(
					new QPostDetailResponseDto(
							post.id,
							post.title,
							post.body,
							post.category,
							post.state,
							post.techs,
							post.images,
							post.heartCount)
				)
				.from(post)
				.orderBy(post.createdDate.desc())
				.fetch();

//		Map<Long, List<PostTechsDto>> postTechsMap = postIdToPostTechsDtoMap(content);



//		content.forEach(dto -> dto.setTechs(postTechsMap.get(dto.getId())));

//		for (PostPagingResponseDto dto : content) {
//			List<PostTechsDto> postTechsDtos = postTechsMap.get(dto.getId());
//			for (PostTechsDto techDto : postTechsDtos) {
//				techList.add(techDto.getTechs().get);
//			}
//			dto.setTechs(postTechsMap.get(dto.getId()));
//		}

		int count = queryFactory.selectFrom(post)
				.where(categoryEq(category))
				.fetch().size();

		return new PageImpl<>(content, pageable, count);
	}

	public Page<PostDetailResponseDto> findAll(Category category, List<Tech> techs, Pageable pageable) {
		List<Post> content = queryFactory.selectFrom(post)
				.where(categoryEq(category)
						.and(containsTechs(techs)))
				.fetch();

		List<PostDetailResponseDto> result = content.stream().map(post -> {
			PostDetailResponseDto dto = new PostDetailResponseDto();
			dto.setId(post.getId());
			dto.setTitle(post.getTitle());
			dto.setBody(post.getBody());
			dto.setCategory(post.getCategory());
			dto.setState(post.getState());
			dto.setTechs(post.getTechs());
			dto.setImages(post.getImages());
			dto.setHeartCount(post.getHeartCount());
			return dto;
		}).toList();

		int count = queryFactory.selectFrom(post)
				.where(categoryEq(category)
						.and(containsTechs(techs)))
				.fetch().size();

		return new PageImpl<>(result, pageable, count);
	}

	private Map<Long, List<PostTechsDto>> postIdToPostTechsDtoMap(List<PostPagingResponseDto> content) {
		List<Long> postIds = content.stream()
				.map(PostPagingResponseDto::getId)
				.toList();

		List<PostTechsDto> postTechsDto = queryFactory.select(
						new QPostTechsDto(
								post.id,
								post.techs
						)
				).from(post)
				.where(post.id.in(postIds))
				.fetch();

		return postTechsDto.stream()
				.collect(Collectors.groupingBy(PostTechsDto::getPostId));
	}

	public PostDetailResponseDto findPostDetailById(Long id) {
		return queryFactory.select(
				new QPostDetailResponseDto(
						post.id,
						post.title,
						post.body,
						post.category,
						post.state,
						post.techs,
						post.images,
						post.heartCount)
				)
				.from(post)
				.where(post.id.eq(id))
				.fetchOne();
	}

	private BooleanExpression categoryEq(Category category) {
		return category != null ? post.category.eq(category) : null;
	}

	private BooleanBuilder containsTechs(List<Tech> techs) {
		BooleanBuilder builder = new BooleanBuilder();

		if (techs == null || techs.isEmpty()) return builder;

		for (Tech tech : techs) {
			builder.and(post.techs.contains(tech));
		}

		return builder;
	}
}
