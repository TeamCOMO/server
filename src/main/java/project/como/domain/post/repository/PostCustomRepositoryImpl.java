package project.como.domain.post.repository;

import static project.como.domain.image.model.QImage.image;
import static project.como.domain.post.model.QPost.post;
import static project.como.domain.post.model.QPostTech.postTech;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import project.como.domain.image.model.Image;
import project.como.domain.post.dto.PostDetailResponseDto;
import project.como.domain.post.dto.PostPagingResponseDto;
import project.como.domain.post.model.Category;
import project.como.domain.post.model.Post;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
	private final JPAQueryFactory queryFactory;

	public Page<PostPagingResponseDto> findAllByCategoryAndTechs(Category category, List<String> stacks, Pageable pageable) {
		List<Post> tmp_posts = queryFactory.selectFrom(post)
				.join(post.techList, postTech).fetchJoin()
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
			dto.setTechs(post.getTechList().stream().map((pt) -> pt.getTech().getStack()).toList());
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
				.join(post.techList, postTech).fetchJoin()
				.leftJoin(post.images, image)
				.where(post.id.eq(id))
				.fetchOne();

		return PostDetailResponseDto.builder()
				.id(result.getId())
				.title(result.getTitle())
				.body(result.getBody())
				.category(result.getCategory())
				.state(result.getState())
				.techs(result.getTechList().stream().distinct().map((t) -> t.getTech().getStack()).sorted().toList())
				.images(result.getImages().stream().map(Image::getUrl).collect(Collectors.toList()))
				.heartCount(result.getHeartCount())
				.build();
	}

	private BooleanExpression categoryEq(Category category) {
		return category != null ? post.category.eq(category) : null;
	}

	private BooleanExpression containsTechs(List<String> stacks) {
		BooleanExpression where = null;
		if (stacks == null || stacks.isEmpty()) return null;

		for (String stack : stacks) {
			BooleanExpression exp = post.techList.any().tech.stack.eq(stack);
			where = (where == null) ? exp : where.or(exp);
		}

		return where;
//		else {
//			builder.and(
//
//					post.techList.contains(JPAExpressions.
//							selectFrom(postTech)
//							.where(postTech.tech.stack.in(stacks)))
//			);
//			for (String tech : stacks) {
//				builder.and(post.techList.contains(
//						JPAExpressions.selectFrom(postTech)
//								.where(postTech.tech.stack.eq(tech))
//				));
////				builder.and(post.techList.in(
////						JPAExpressions
////								.select(postTech.tech.stack)
////								.from(post)
////								.join(post.techList, postTech)
////								.where(postTech.tech.stack.eq(tech))
////				));
//			}
		// 하나라도 포함되면 Post의 조건에 맞도록 해야함.
		// 예를 들어, Java, Spring 을 골랐을 때 둘 다 있는 Post만 제시하는 것이 아니라 Java만 있는 게시물과 Spring만 있는 게시물, 둘 다 포함하는 게사물 모두 제시해야함.
	}
}
