package project.como.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import project.como.domain.image.model.Image;
import project.como.domain.post.dto.*;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;
import project.como.domain.post.model.Tech;

import java.util.List;
import java.util.stream.Collectors;

import static project.como.domain.image.model.QImage.*;
import static project.como.domain.post.model.QPost.*;
import static project.como.domain.post.model.QTech.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
	private final JPAQueryFactory queryFactory;

	public Page<PostPagingResponseDto> findAllByCategoryAndTechs(Category category, List<String> stacks, Pageable pageable) {
		List<Post> tmp_posts = queryFactory.selectFrom(post)
				.join(post.techs)
				.where(categoryEq(category)
						.and(containsTechs(stacks)))
				.orderBy(post.createdDate.desc())
				.fetch();

		List<Post> posts = tmp_posts.subList((int)pageable.getOffset(), Math.min((int)pageable.getOffset() + pageable.getPageSize(), tmp_posts.size()));

		List<PostPagingResponseDto> content = posts.stream().map(post -> {
			PostPagingResponseDto dto = new PostPagingResponseDto();
			dto.setId(post.getId());
			dto.setTitle(post.getTitle());
			dto.setCategory(post.getCategory());
			dto.setState(post.getState());
			dto.setTechs(post.getTechs().stream().map(Tech::getStack).collect(Collectors.toList()));
			dto.setHeartCount(post.getHeartCount());
			return dto;
		}).toList();

		JPAQuery<Long> countQuery = queryFactory.select(post.count())
				.from(post)
				.where(categoryEq(category)
						.and(containsTechs(stacks)))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize());

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	public PostDetailResponseDto findPostDetailById(Long id) {
		Post result = queryFactory.selectFrom(post)
				.join(post.techs, tech)
				.leftJoin(post.images, image)
				.where(post.id.eq(id))
				.fetchOne();

		return PostDetailResponseDto.builder()
				.id(result.getId())
				.title(result.getTitle())
				.body(result.getBody())
				.category(result.getCategory())
				.state(result.getState())
				.techs(result.getTechs().stream().map(Tech::getStack).collect(Collectors.toList()))
				.images(result.getImages().stream().map(Image::getUrl).collect(Collectors.toList()))
				.heartCount(result.getHeartCount())
				.build();
	}

	private BooleanExpression categoryEq(Category category) {
		return category != null ? post.category.eq(category) : null;
	}

	private BooleanBuilder containsTechs(List<String> stacks) {
		BooleanBuilder builder = new BooleanBuilder();

		if (stacks == null || stacks.isEmpty()) return builder;
		else {
			builder.and(
					post.in(
							JPAExpressions.select(tech.post)
									.from(tech)
									.where(tech.stack.in(stacks))
					)
			);
		}

		return builder;
	}
}
